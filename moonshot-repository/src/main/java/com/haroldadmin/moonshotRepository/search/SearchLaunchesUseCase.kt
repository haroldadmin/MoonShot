package com.haroldadmin.moonshotRepository.search

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshotRepository.launch.PersistLaunchesUseCase
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class SearchLaunchesUseCase(
    private val launchesDao: LaunchDao,
    private val launchesService: LaunchesService,
    persistLaunchesUseCase: PersistLaunchesUseCase
) : SearchUseCase() {

    private val resource = searchResource(
        dbQuery = { _query },
        dbLimit = { _limit },
        dbFetcher = { _, query, limit -> getSearchResultsCached(query, limit) },
        cacheValidator = { cached -> !cached.isNullOrEmpty() },
        apiFetcher = { getAllLaunchesFromApi() },
        dataPersister = persistLaunchesUseCase::persistLaunches
    )

    @ExperimentalCoroutinesApi
    fun searchFor(query: String, limit: Int): Flow<Resource<List<LaunchMinimal>>> {
        _query = query
        _limit = limit
        return resource.flow()
    }

    private suspend fun getSearchResultsCached(query: String, limit: Int) =
        withContext(Dispatchers.IO) {
            launchesDao.getLaunchesForQuery("%$query%", limit)
        }

    private suspend fun getAllLaunchesFromApi() = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchesService.getAllLaunches().await()
        }
    }
}