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

class GetLaunchDetailsUseCase(
    private val launchesDao: LaunchDao,
    private val launchesService: LaunchesService,
    private val persistLaunchesUseCase: PersistLaunchesUseCase
) {

    private lateinit var launchDetailsRes: SingleFetchNetworkBoundResource<Launch, ApiLaunch, ErrorResponse>

    @ExperimentalCoroutinesApi
    fun getLaunchDetails(flightNumber: Int): Flow<Resource<Launch>> {
        if (!::launchDetailsRes.isInitialized) {
            launchDetailsRes = singleFetchNetworkBoundResource(
                dbFetcher = { getLaunchDetailsCached(flightNumber) },
                cacheValidator = { cached -> cached != null },
                apiFetcher = { getLaunchDetailsFromApi(flightNumber) },
                dataPersister = persistLaunchesUseCase::persistLaunch
            )
        }
        return launchDetailsRes.flow()
    }

    private suspend fun getLaunchDetailsCached(flightNumber: Int) = withContext(Dispatchers.IO) {
        launchesDao.details(flightNumber)
    }

    private suspend fun getLaunchDetailsFromApi(flightNumber: Int) = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchesService.getLaunch(flightNumber).await()
        }
    }
}