package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.AppDispatchers
import com.haroldadmin.moonshot.core.Resource
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

class GetLaunchDetailsUseCase @Inject constructor(
    private val launchesDao: LaunchDao,
    private val launchesService: LaunchesService,
    private val persistLaunchesUseCase: PersistLaunchesUseCase,
    private val appDispatchers: AppDispatchers
) {

    private var flightNumber: Int = 0

    @ExperimentalCoroutinesApi
    private val launchDetailsRes: SingleFetchNetworkBoundResource<Launch, ApiLaunch, ErrorResponse> by singleFetchNetworkBoundResourceLazy(
        dbFetcher = { _, _, _ -> getLaunchDetailsCached(flightNumber) },
        cacheValidator = { cached -> cached != null },
        apiFetcher = { getLaunchDetailsFromApi(flightNumber) },
        dataPersister = persistLaunchesUseCase::persistLaunch
    )

    @ExperimentalCoroutinesApi
    fun getLaunchDetails(flightNumber: Int): Flow<Resource<Launch>> {
        this.flightNumber = flightNumber
        return launchDetailsRes.flow()
    }

    private suspend fun getLaunchDetailsCached(flightNumber: Int) = withContext(appDispatchers.IO) {
        launchesDao.details(flightNumber)
    }

    private suspend fun getLaunchDetailsFromApi(flightNumber: Int) = withContext(appDispatchers.IO) {
        executeWithRetry {
            launchesService.getLaunch(flightNumber).await()
        }
    }
}