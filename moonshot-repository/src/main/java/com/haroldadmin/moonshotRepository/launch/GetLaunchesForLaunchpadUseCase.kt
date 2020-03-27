package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.AppDispatchers
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.pairOf
import com.haroldadmin.moonshot.database.LaunchDao
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshotRepository.SingleFetchNetworkBoundResource
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundResourceLazy
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import com.haroldadmin.spacex_api_wrapper.launches.Launch as ApiLaunch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetLaunchesForLaunchpadUseCase @Inject constructor(
    private val launchesDao: LaunchDao,
    private val launchesService: LaunchesService,
    private val persistLaunchesUseCase: PersistLaunchesUseCase,
    private val appDispatchers: AppDispatchers
) {

    private val defaultLimit = 15
    private val defaultOffset = 0
    private var siteId: String = ""

    @ExperimentalCoroutinesApi
    private val allLaunchesRes: SingleFetchNetworkBoundResource<List<Launch>, List<ApiLaunch>, ErrorResponse> by singleFetchNetworkBoundResourceLazy(
        initialParams = ::initialParams,
        dbFetcher = { _, limit, offset -> getAllCachedLaunches(siteId, limit, offset) },
        cacheValidator = { cachedLaunches -> !cachedLaunches.isNullOrEmpty() },
        apiFetcher = { getAllLaunchesFromService(siteId) },
        dataPersister = { launches -> persistLaunchesUseCase.persistLaunches(launches) }
    )
    @ExperimentalCoroutinesApi
    private val pastLaunchesRes: SingleFetchNetworkBoundResource<List<Launch>, List<ApiLaunch>, ErrorResponse> by singleFetchNetworkBoundResourceLazy(
        initialParams = ::initialParams,
        dbFetcher = { _, limit, offset -> getPastCachedLaunches(siteId, limit, offset) },
        cacheValidator = { cachedData -> !cachedData.isNullOrEmpty() },
        apiFetcher = { getPastLaunchesFromService(siteId) },
        dataPersister = { launches -> persistLaunchesUseCase.persistLaunches(launches) }
    )
    @ExperimentalCoroutinesApi
    private val upcomingLaunchesRes: SingleFetchNetworkBoundResource<List<Launch>, List<ApiLaunch>, ErrorResponse> by singleFetchNetworkBoundResourceLazy(
        initialParams = ::initialParams,
        dbFetcher = { _, limit, offset -> getUpcomingCachedLaunches(siteId, limit, offset) },
        cacheValidator = { cachedLaunches -> !cachedLaunches.isNullOrEmpty() },
        apiFetcher = { getUpcomingLaunchesFromService(siteId) },
        dataPersister = { launches -> persistLaunchesUseCase.persistLaunches(launches) }
    )

    @ExperimentalCoroutinesApi
    fun getLaunchesForLaunchpad(
        siteId: String,
        type: LaunchType,
        limit: Int = defaultLimit,
        offset: Int = defaultOffset
    ): Flow<Resource<List<Launch>>> {
        this.siteId = siteId
        return when (type) {
            LaunchType.All -> getAllLaunches(limit, offset)
            LaunchType.Recent -> getPastLaunches(limit, offset)
            LaunchType.Upcoming -> getUpcomingLaunches(limit, offset)
        }
    }

    @ExperimentalCoroutinesApi
    private fun getAllLaunches(limit: Int, offset: Int): Flow<Resource<List<Launch>>> {
        allLaunchesRes.updateParams(limit, offset)
        return allLaunchesRes.flow()
    }

    @ExperimentalCoroutinesApi
    private fun getPastLaunches(limit: Int, offset: Int): Flow<Resource<List<Launch>>> {
        pastLaunchesRes.updateParams(limit, offset)
        return pastLaunchesRes.flow()
    }

    @ExperimentalCoroutinesApi
    private fun getUpcomingLaunches(limit: Int, offset: Int): Flow<Resource<List<Launch>>> {
        upcomingLaunchesRes.updateParams(limit, offset)
        return upcomingLaunchesRes.flow()
    }

    private suspend fun getAllCachedLaunches(
        siteId: String,
        limit: Int,
        offset: Int
    ): List<Launch> = withContext(appDispatchers.IO) {
        launchesDao.forLaunchPad(siteId, limit, offset)
    }

    private suspend fun getPastCachedLaunches(
        siteId: String,
        limit: Int,
        offset: Int
    ): List<Launch> = withContext(appDispatchers.IO) {
        launchesDao.forLaunchPad(siteId, false, limit, offset)
    }

    private suspend fun getUpcomingCachedLaunches(
        siteId: String,
        limit: Int,
        offset: Int
    ): List<Launch> = withContext(appDispatchers.IO) {
        launchesDao.forLaunchPad(siteId, true, limit, offset)
    }

    private suspend fun getAllLaunchesFromService(
        siteId: String
    ): NetworkResponse<List<ApiLaunch>, ErrorResponse> = withContext(appDispatchers.IO) {
        executeWithRetry {
            launchesService.getAllLaunches(siteId = siteId).await()
        }
    }

    private suspend fun getUpcomingLaunchesFromService(
        siteId: String
    ): NetworkResponse<List<ApiLaunch>, ErrorResponse> = withContext(appDispatchers.IO) {
        executeWithRetry {
            launchesService.getUpcomingLaunches(siteId = siteId).await()
        }
    }

    private suspend fun getPastLaunchesFromService(siteId: String) = withContext(appDispatchers.IO) {
        executeWithRetry {
            launchesService.getPastLaunches(siteId = siteId).await()
        }
    }

    private fun initialParams(): Pair<Int, Int> {
        val limit = 15
        val offset = 0
        return pairOf(limit, offset)
    }
}