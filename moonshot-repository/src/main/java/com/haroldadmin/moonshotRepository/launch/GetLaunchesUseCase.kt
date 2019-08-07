package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshotRepository.networkBoundFlow
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

enum class LaunchesFilter {
    PAST, UPCOMING, ALL
}
class GetLaunchesUseCase(
    launchesDao: LaunchDao,
    launchesService: LaunchesService
) : LaunchesUseCase(launchesDao, launchesService) {

    suspend fun getLaunches(
        filter: LaunchesFilter,
        currentTime: Long,
        limit: Int = 15
    ): Flow<Resource<List<LaunchMinimal>>> {
        return when (filter) {
            LaunchesFilter.PAST -> getPastLaunches(currentTime, limit)
            LaunchesFilter.UPCOMING -> getUpcomingLaunches(currentTime, limit)
            LaunchesFilter.ALL -> getAllLaunches(limit)
        }
    }

    internal suspend fun getPastLaunches(
        currentTime: Long,
        limit: Int
    ): Flow<Resource<List<LaunchMinimal>>> {
        return networkBoundFlow(
            dbFetcher = { getPastCachedLaunches(currentTime, limit) },
            cacheValidator = { cachedData -> !cachedData.isNullOrEmpty() },
            apiFetcher = { getPastApiLaunches() },
            dataPersister = this::persistLaunches
        )
    }

    internal suspend fun getUpcomingLaunches(
        currentTime: Long,
        limit: Int
    ): Flow<Resource<List<LaunchMinimal>>> {
        return networkBoundFlow(
            dbFetcher = { getUpcomingCachedLaunches(currentTime, limit) },
            cacheValidator = { cachedData -> !cachedData.isNullOrEmpty() },
            apiFetcher = { getUpcomingApiLaunches() },
            dataPersister = this::persistLaunches
        )
    }


    internal suspend fun getAllLaunches(limit: Int): Flow<Resource<List<LaunchMinimal>>> {
        return networkBoundFlow(
            dbFetcher = { getAllCachedLaunches(limit) },
            cacheValidator = { cachedData -> !cachedData.isNullOrEmpty() },
            apiFetcher = { getAllApiLaunches() },
            dataPersister = this::persistLaunches
        )
    }

    private suspend fun getPastCachedLaunches(currentTime: Long, limit: Int) =
        withContext(Dispatchers.IO) {
            launchesDao.getPastLaunches(currentTime, limit)
        }

    private suspend fun getPastApiLaunches() = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchesService.getPastLaunches().await()
        }
    }

    private suspend fun getUpcomingCachedLaunches(currentTime: Long, limit: Int) =
        withContext(Dispatchers.IO) {
            launchesDao.getUpcomingLaunches(currentTime, limit)
        }

    private suspend fun getUpcomingApiLaunches() = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchesService.getUpcomingLaunches().await()
        }
    }

    private suspend fun getAllCachedLaunches(limit: Int) = withContext(Dispatchers.IO) {
        launchesDao.getAllLaunches(limit)
    }

    private suspend fun getAllApiLaunches() = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchesService.getAllLaunches().await()
        }
    }
}