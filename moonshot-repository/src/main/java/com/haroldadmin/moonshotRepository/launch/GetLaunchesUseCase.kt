package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.LaunchDao
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundFlow
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetLaunchesUseCase(
    private val launchesDao: LaunchDao,
    private val launchesService: LaunchesService,
    private val persistLaunchesUseCase: PersistLaunchesUseCase
) {

    @ExperimentalCoroutinesApi
    fun getLaunches(
        filter: LaunchType,
        limit: Int = 15,
        offset: Int = 0
    ): Flow<Resource<List<Launch>>> {
        return when (filter) {
            LaunchType.Recent -> getPastLaunches(limit, offset)
            LaunchType.Upcoming -> getUpcomingLaunches(limit, offset)
            LaunchType.All -> getAllLaunches(limit, offset)
        }
    }

    suspend fun sync(): Resource<Unit> {
        val apiLaunches = executeWithRetry {
            launchesService.getAllLaunches().await()
        }

        return when (apiLaunches) {
            is NetworkResponse.Success -> {
                persistLaunchesUseCase.persistLaunches(apiLaunches.body, shouldSynchronize = true)
                Resource.Success(Unit)
            }
            is NetworkResponse.ServerError -> Resource.Error(Unit, null)
            is NetworkResponse.NetworkError -> Resource.Error(Unit, null)
        }
    }

    @ExperimentalCoroutinesApi
    private fun getPastLaunches(
        limit: Int,
        offset: Int
    ): Flow<Resource<List<Launch>>> {
        return singleFetchNetworkBoundFlow(
            dbFetcher = { getPastCachedLaunches(limit, offset) },
            cacheValidator = { cachedData -> !cachedData.isNullOrEmpty() },
            apiFetcher = { getPastApiLaunches() },
            dataPersister = { launches -> persistLaunchesUseCase.persistLaunches(launches) }
        )
    }

    @ExperimentalCoroutinesApi
    private fun getUpcomingLaunches(limit: Int, offset: Int): Flow<Resource<List<Launch>>> {
        return singleFetchNetworkBoundFlow(
            dbFetcher = { getUpcomingCachedLaunches(limit, offset) },
            cacheValidator = { cachedData -> !cachedData.isNullOrEmpty() },
            apiFetcher = { getUpcomingApiLaunches() },
            dataPersister = { launches -> persistLaunchesUseCase.persistLaunches(launches) }
        )
    }

    @ExperimentalCoroutinesApi
    private fun getAllLaunches(limit: Int, offset: Int): Flow<Resource<List<Launch>>> {
        return singleFetchNetworkBoundFlow(
            dbFetcher = { getAllCachedLaunches(limit, offset) },
            cacheValidator = { cachedData -> !cachedData.isNullOrEmpty() },
            apiFetcher = { getAllApiLaunches() },
            dataPersister = { launches -> persistLaunchesUseCase.persistLaunches(launches) }
        )
    }

    private suspend fun getPastCachedLaunches(limit: Int, offset: Int) = withContext(Dispatchers.IO) {
        launchesDao.recent(limit, offset)
    }

    private suspend fun getPastApiLaunches() = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchesService.getPastLaunches().await()
        }
    }

    private suspend fun getUpcomingCachedLaunches(limit: Int, offset: Int) = withContext(Dispatchers.IO) {
        launchesDao.upcoming(limit, offset)
    }

    private suspend fun getUpcomingApiLaunches() = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchesService.getUpcomingLaunches().await()
        }
    }

    private suspend fun getAllCachedLaunches(limit: Int, offset: Int) = withContext(Dispatchers.IO) {
        launchesDao.all(limit, offset)
    }

    private suspend fun getAllApiLaunches() = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchesService.getAllLaunches().await()
        }
    }
}