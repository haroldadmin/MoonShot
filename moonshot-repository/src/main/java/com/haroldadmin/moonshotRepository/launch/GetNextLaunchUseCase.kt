package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.LaunchDao
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshotRepository.SingleFetchNetworkBoundResource
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundFlow
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundResource
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import com.haroldadmin.spacex_api_wrapper.launches.Launch as ApiLaunch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetNextLaunchUseCase(
    private val launchesDao: LaunchDao,
    private val launchesService: LaunchesService,
    private val persistLaunchesUseCase: PersistLaunchesUseCase
) {

    private lateinit var nextLaunchRes: SingleFetchNetworkBoundResource<Launch, ApiLaunch, ErrorResponse>
    private lateinit var nextLaunchesUntilDateRes: SingleFetchNetworkBoundResource<List<Launch>, List<ApiLaunch>, ErrorResponse>

    @ExperimentalCoroutinesApi
    fun getNextLaunch(): Flow<Resource<Launch>> {
        if (!::nextLaunchRes.isInitialized) {
            nextLaunchRes = singleFetchNetworkBoundResource(
                dbFetcher = { getNextLaunchCached() },
                cacheValidator = { cachedData -> cachedData != null },
                apiFetcher = { getNextLaunchFromService() },
                dataPersister = persistLaunchesUseCase::persistLaunch
            )
        }
        return nextLaunchRes.flow()
   }

    @ExperimentalCoroutinesApi
    fun getNextLaunchesUntilDate(until: Long, limit: Int = 10, offset: Int = 0): Flow<Resource<List<Launch>>> {
        if (!::nextLaunchesUntilDateRes.isInitialized) {
            nextLaunchesUntilDateRes = singleFetchNetworkBoundResource(
                dbFetcher = { getNextLaunchesUntilDateCached(until, limit, offset) },
                cacheValidator = { cachedData -> !cachedData.isNullOrEmpty() },
                apiFetcher = { getAllUpcomingLaunchesFromApi() },
                dataPersister = { launches -> persistLaunchesUseCase.persistLaunches(launches) }
            )
        }
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
}