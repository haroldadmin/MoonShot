package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.pairOf
import com.haroldadmin.moonshot.database.LaunchDao
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshotRepository.NetworkBoundResource
import com.haroldadmin.moonshotRepository.SingleFetchNetworkBoundResource
import com.haroldadmin.moonshotRepository.networkBoundResourceLazy
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundResourceLazy
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import com.haroldadmin.spacex_api_wrapper.launches.Launch as ApiLaunch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetNextLaunchUseCase @Inject constructor(
    private val launchesDao: LaunchDao,
    private val launchesService: LaunchesService,
    private val persistLaunchesUseCase: PersistLaunchesUseCase
) {

    private var untilTime: Long = Long.MAX_VALUE
    private val defaultLimit = 10
    private val defaultOffset = 0

    @ExperimentalCoroutinesApi
    private val nextLaunchRes: NetworkBoundResource<Launch, ApiLaunch, ErrorResponse> by networkBoundResourceLazy(
        dbFetcher = { _, _, _ -> getNextLaunchCached() },
        cacheValidator = { cachedData -> cachedData != null },
        apiFetcher = { getNextLaunchFromService() },
        dataPersister = persistLaunchesUseCase::persistLaunch
    )

    @ExperimentalCoroutinesApi
    private val nextLaunchesUntilDateRes: SingleFetchNetworkBoundResource<List<Launch>, List<ApiLaunch>, ErrorResponse> by singleFetchNetworkBoundResourceLazy(
        initialParams = ::initialParams,
        dbFetcher = { _, limit, offset -> getNextLaunchesUntilDateCached(untilTime, limit, offset) },
        cacheValidator = { cachedData -> !cachedData.isNullOrEmpty() },
        apiFetcher = { getAllUpcomingLaunchesFromApi() },
        dataPersister = { launches -> persistLaunchesUseCase.persistLaunches(launches) }
    )

    @ExperimentalCoroutinesApi
    fun getNextLaunch(): Flow<Resource<Launch>> {
        return nextLaunchRes.flow()
    }

    @ExperimentalCoroutinesApi
    fun getNextLaunchesUntilDate(
        until: Long,
        limit: Int = defaultLimit,
        offset: Int = defaultOffset
    ): Flow<Resource<List<Launch>>> {
        this.untilTime = until
        nextLaunchesUntilDateRes.updateParams(limit, offset)
        return nextLaunchesUntilDateRes.flow()
    }

    private suspend fun getNextLaunchCached() = withContext(Dispatchers.IO) {
        launchesDao.next()
    }

    private suspend fun getNextLaunchFromService() = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchesService.getNextLaunch().await()
        }
    }

    private suspend fun getNextLaunchesUntilDateCached(until: Long, limit: Int, offset: Int) =
        withContext(Dispatchers.IO) {
            launchesDao.upcoming(until, limit, offset)
        }

    private suspend fun getAllUpcomingLaunchesFromApi() = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchesService.getUpcomingLaunches().await()
        }
    }

    private fun initialParams() = pairOf(defaultLimit, defaultOffset)
}