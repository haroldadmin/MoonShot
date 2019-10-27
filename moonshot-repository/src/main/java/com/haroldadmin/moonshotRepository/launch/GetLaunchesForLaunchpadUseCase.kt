package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.LaunchDao
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshotRepository.networkBoundFlow
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import com.haroldadmin.spacex_api_wrapper.launches.Launch as ApiLaunch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetLaunchesForLaunchpadUseCase(
    private val launchesDao: LaunchDao,
    private val launchesService: LaunchesService,
    private val persistLaunchesUseCase: PersistLaunchesUseCase
) {

    @ExperimentalCoroutinesApi
    fun getLaunchesForLaunchpad(
        siteId: String,
        type: LaunchType,
        limit: Int = 15,
        offset: Int = 0
    ): Flow<Resource<List<Launch>>> {
        return when (type) {
            LaunchType.All -> getAllLaunches(siteId, limit, offset)
            LaunchType.Recent -> getPastLaunches(siteId, limit, offset)
            LaunchType.Upcoming -> getUpcomingLaunches(siteId, limit, offset)
        }
    }

    @ExperimentalCoroutinesApi
    private fun getAllLaunches(siteId: String, limit: Int, offset: Int): Flow<Resource<List<Launch>>> {
        return networkBoundFlow(
            dbFetcher = { getAllCachedLaunches(siteId, limit, offset) },
            cacheValidator = { cachedLaunches -> !cachedLaunches.isNullOrEmpty() },
            apiFetcher = { getAllLaunchesFromService(siteId) },
            dataPersister = { launches -> persistLaunchesUseCase.persistLaunches(launches) }
        )
    }

    @ExperimentalCoroutinesApi
    private fun getPastLaunches(siteId: String, limit: Int, offset: Int): Flow<Resource<List<Launch>>> {
        return networkBoundFlow(
            dbFetcher = { getPastCachedLaunches(siteId, limit, offset) },
            cacheValidator = { cachedData -> !cachedData.isNullOrEmpty() },
            apiFetcher = { getPastLaunchesFromService(siteId) },
            dataPersister = { launches -> persistLaunchesUseCase.persistLaunches(launches) }
        )
    }

    @ExperimentalCoroutinesApi
    private fun getUpcomingLaunches(siteId: String, limit: Int, offset: Int): Flow<Resource<List<Launch>>> {
        return networkBoundFlow(
            dbFetcher = { getUpcomingCachedLaunches(siteId, limit, offset) },
            cacheValidator = { cachedLaunches -> !cachedLaunches.isNullOrEmpty() },
            apiFetcher = { getUpcomingLaunchesFromService(siteId) },
            dataPersister = { launches -> persistLaunchesUseCase.persistLaunches(launches) }
        )
    }

    private suspend fun getAllCachedLaunches(
        siteId: String,
        limit: Int,
        offset: Int
    ): List<Launch> = withContext(Dispatchers.IO) {
        launchesDao.forLaunchPad(siteId, limit, offset)
    }

    private suspend fun getPastCachedLaunches(
        siteId: String,
        limit: Int,
        offset: Int
    ): List<Launch> = withContext(Dispatchers.IO) {
        launchesDao.forLaunchPad(siteId, false, limit, offset)
    }

    private suspend fun getUpcomingCachedLaunches(
        siteId: String,
        limit: Int,
        offset: Int
    ): List<Launch> = withContext(Dispatchers.IO) {
        launchesDao.forLaunchPad(siteId, true, limit, offset)
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