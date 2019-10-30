package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.pairOf
import com.haroldadmin.moonshot.database.LaunchDao
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshotRepository.SingleFetchNetworkBoundResource
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundResourceLazy
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import com.haroldadmin.spacex_api_wrapper.launches.Launch as ApiLaunch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetLaunchesUseCase @Inject constructor(
    private val launchesDao: LaunchDao,
    private val launchesService: LaunchesService,
    private val persistLaunchesUseCase: PersistLaunchesUseCase
) {

    private val defaultLimit = 15
    private val defaultOffset = 0

    @ExperimentalCoroutinesApi
    private val pastLaunchesRes: SingleFetchNetworkBoundResource<List<Launch>, List<ApiLaunch>, ErrorResponse> by singleFetchNetworkBoundResourceLazy(
        initialParams = ::initialParams,
        dbFetcher = { _, dbLimit, dbOffset -> getPastCachedLaunches(dbLimit, dbOffset) },
        cacheValidator = { cachedData -> !cachedData.isNullOrEmpty() },
        apiFetcher = { getPastApiLaunches() },
        dataPersister = { launches -> persistLaunchesUseCase.persistLaunches(launches) }
    )

    @ExperimentalCoroutinesApi
    private val upcomingLaunchesRes: SingleFetchNetworkBoundResource<List<Launch>, List<ApiLaunch>, ErrorResponse> by singleFetchNetworkBoundResourceLazy(
        initialParams = ::initialParams,
        dbFetcher = { _, dbLimit, dbOffset -> getUpcomingCachedLaunches(dbLimit, dbOffset) },
        cacheValidator = { cachedData -> !cachedData.isNullOrEmpty() },
        apiFetcher = { getUpcomingApiLaunches() },
        dataPersister = { launches -> persistLaunchesUseCase.persistLaunches(launches) }
    )

    @ExperimentalCoroutinesApi
    private val allLaunchesRes: SingleFetchNetworkBoundResource<List<Launch>, List<ApiLaunch>, ErrorResponse> by singleFetchNetworkBoundResourceLazy(
        initialParams = ::initialParams,
        dbFetcher = { _, dbLimit, dbOffset -> getAllCachedLaunches(dbLimit, dbOffset) },
        cacheValidator = { cachedData -> !cachedData.isNullOrEmpty() },
        apiFetcher = { getAllApiLaunches() },
        dataPersister = { launches -> persistLaunchesUseCase.persistLaunches(launches) }
    )

    @ExperimentalCoroutinesApi
    fun getLaunches(
        filter: LaunchType,
        limit: Int = defaultLimit,
        offset: Int = defaultOffset
    ): Flow<Resource<List<Launch>>> {
        return when (filter) {
            LaunchType.Recent -> getPastLaunches(limit, offset)
            LaunchType.Upcoming -> getUpcomingLaunches(limit, offset)
            LaunchType.All -> getAllLaunches(limit, offset)
        }
    }

    suspend fun sync(): Resource<Unit> = when (val apiLaunches = getAllApiLaunches()) {
        is NetworkResponse.Success -> {
            persistLaunchesUseCase.persistLaunches(apiLaunches.body, shouldSynchronize = true)
            Resource.Success(Unit)
        }
        else -> Resource.Error(Unit, null)
    }

    @ExperimentalCoroutinesApi
    private fun getPastLaunches(
        limit: Int,
        offset: Int
    ): Flow<Resource<List<Launch>>> {
        pastLaunchesRes.updateParams(limit, offset)
        return pastLaunchesRes.flow()
    }

    @ExperimentalCoroutinesApi
    private fun getUpcomingLaunches(limit: Int, offset: Int): Flow<Resource<List<Launch>>> {
        upcomingLaunchesRes.updateParams(limit, offset)
        return upcomingLaunchesRes.flow()
    }

    @ExperimentalCoroutinesApi
    private fun getAllLaunches(limit: Int, offset: Int): Flow<Resource<List<Launch>>> {
        allLaunchesRes.updateParams(limit, offset)
        return allLaunchesRes.flow()
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

    private fun initialParams(): Pair<Int, Int> = pairOf(defaultLimit, defaultOffset)
}