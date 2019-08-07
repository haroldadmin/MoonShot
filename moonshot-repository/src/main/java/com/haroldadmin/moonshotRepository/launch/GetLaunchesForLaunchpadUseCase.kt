package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshotRepository.networkBoundFlow
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import com.haroldadmin.spacex_api_wrapper.launches.Launch as ApiLaunch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetLaunchesForLaunchpadUseCase(
    dao: LaunchDao,
    service: LaunchesService
) : LaunchesUseCase(dao, service) {

    suspend fun getLaunchesForLaunchpad(
        siteId: String,
        from: Long,
        to: Long,
        currentTime: Long,
        limit: Int = 15
    ): Flow<Resource<List<LaunchMinimal>>> {

        require(from <= to) {
            "Beginning ($from) of time range should be before the end ($to)"
        }

        return when {
            to <= currentTime -> getPastLaunches(siteId, currentTime, limit)
            from > currentTime -> getUpcomingLaunches(siteId, currentTime, limit)
            else -> getAllLaunches(siteId, limit)
        }
    }

    internal suspend fun getAllLaunches(siteId: String, limit: Int): Flow<Resource<List<LaunchMinimal>>> {
        return networkBoundFlow(
            dbFetcher = { getAllCachedLaunches(siteId, limit) },
            cacheValidator = { cachedLaunches -> !cachedLaunches.isNullOrEmpty() },
            apiFetcher = { getAllLaunchesFromService(siteId) },
            dataPersister = this::persistLaunches
        )
    }

    internal suspend fun getPastLaunches(siteId: String, currentTime: Long, limit: Int): Flow<Resource<List<LaunchMinimal>>> {
        return networkBoundFlow(
            dbFetcher = { getPastCachedLaunches(siteId, currentTime, limit) },
            cacheValidator = { cachedData -> !cachedData.isNullOrEmpty() },
            apiFetcher = { getPastLaunchesFromService(siteId) },
            dataPersister = this::persistLaunches
        )
    }

    internal suspend fun getUpcomingLaunches(siteId: String, currentTime: Long, limit: Int): Flow<Resource<List<LaunchMinimal>>> {
        return networkBoundFlow(
            dbFetcher = { getUpcomingCachedLaunches(siteId, currentTime, limit) },
            cacheValidator = { cachedLaunches -> !cachedLaunches.isNullOrEmpty() },
            apiFetcher = { getUpcomingLaunchesFromService(siteId) },
            dataPersister = this::persistLaunches
        )
    }

    private suspend fun getAllCachedLaunches(
        siteId: String,
        limit: Int
    ): List<LaunchMinimal> = withContext(Dispatchers.IO) {
        launchesDao.getAllLaunchesForLaunchPad(siteId, limit)
    }

    private suspend fun getPastCachedLaunches(
        siteId: String,
        maxTimeStamp: Long,
        limit: Int
    ): List<LaunchMinimal> = withContext(Dispatchers.IO) {
        launchesDao.getPastLaunchesForLaunchPad(siteId, maxTimeStamp, limit)
    }

    private suspend fun getUpcomingCachedLaunches(
        siteId: String,
        minTimeStamp: Long,
        limit: Int
    ): List<LaunchMinimal> = withContext(Dispatchers.IO) {
        launchesDao.getUpcomingLaunchesForLaunchPad(siteId, minTimeStamp, limit)
    }

    private suspend fun getAllLaunchesFromService(
        siteId: String
    ): NetworkResponse<List<ApiLaunch>, ErrorResponse> = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchesService.getAllLaunches(siteId = siteId).await()
        }
    }

    private suspend fun getUpcomingLaunchesFromService(
        siteId: String
    ): NetworkResponse<List<ApiLaunch>, ErrorResponse> = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchesService.getUpcomingLaunches(siteId = siteId).await()
        }
    }

    private suspend fun getPastLaunchesFromService(siteId: String) = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchesService.getPastLaunches(siteId = siteId).await()
        }
    }
}