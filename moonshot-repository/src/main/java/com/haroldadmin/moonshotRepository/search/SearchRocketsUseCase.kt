package com.haroldadmin.moonshotRepository.search

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.rocket.RocketsDao
import com.haroldadmin.moonshot.models.rocket.RocketMinimal
import com.haroldadmin.moonshotRepository.rocket.PersistRocketsUseCase
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SearchRocketsUseCase(
    private val rocketsDao: RocketsDao,
    private val rocketsService: RocketsService,
    persistRocketsUseCase: PersistRocketsUseCase
) : SearchUseCase() {

    @ExperimentalCoroutinesApi
    private val resource = searchResource(
        dbQuery = { _query },
        dbLimit = { _limit },
        dbFetcher = { _, query, limit -> getSearchResultsCached(query, limit) },
        cacheValidator = { cached -> !cached.isNullOrEmpty() },
        apiFetcher = { getAllRocketsFromApi() },
        dataPersister = persistRocketsUseCase::persistApiRockets
    )

    @ExperimentalCoroutinesApi
    fun searchFor(query: String, limit: Int): Flow<Resource<List<RocketMinimal>>> {
        _query = query
        _limit = limit
        return resource.flow()
    }

    private suspend fun getSearchResultsCached(query: String, limit: Int) =
        withContext(Dispatchers.IO) {
            rocketsDao.getRocketsForQuery("%$query%", limit)
        }

    private suspend fun getAllRocketsFromApi() = withContext(Dispatchers.IO) {
        executeWithRetry {
            rocketsService.getAllRockets().await()
        }
    }
}