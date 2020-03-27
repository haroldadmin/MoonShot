package com.haroldadmin.moonshotRepository.search

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.AppDispatchers
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.RocketsDao
import com.haroldadmin.moonshot.models.SearchQuery
import com.haroldadmin.moonshot.models.Rocket
import com.haroldadmin.moonshotRepository.rocket.PersistRocketsUseCase
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRocketsUseCase @Inject constructor(
    private val rocketsDao: RocketsDao,
    private val rocketsService: RocketsService,
    persistRocketsUseCase: PersistRocketsUseCase,
    private val appDispatchers: AppDispatchers
) {

    @ExperimentalCoroutinesApi
    private val resource by searchResourceLazy(
        dbFetcher = { _, query, limit, offset -> getSearchResultsCached(query, limit, offset) },
        cacheValidator = { cached -> !cached.isNullOrEmpty() },
        apiFetcher = { getAllRocketsFromApi() },
        dataPersister = { rockets -> persistRocketsUseCase.persistRockets(rockets) }
    )

    @ExperimentalCoroutinesApi
    fun searchFor(query: SearchQuery, limit: Int): Flow<Resource<List<Rocket>>> {
        resource.updateParams(query, limit)
        return resource.flow()
    }

    private suspend fun getSearchResultsCached(query: String, limit: Int, offset: Int) =
        withContext(appDispatchers.IO) {
            rocketsDao.forQuery(query, limit)
        }

    private suspend fun getAllRocketsFromApi() = withContext(appDispatchers.IO) {
        executeWithRetry {
            rocketsService.getAllRockets().await()
        }
    }
}