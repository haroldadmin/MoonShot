package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshotRepository.networkBoundFlow
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetLaunchDetailsUseCase(
    private val launchesDao: LaunchDao,
    private val launchesService: LaunchesService,
    private val persistLaunchesUseCase: PersistLaunchesUseCase
) {

    @ExperimentalCoroutinesApi
    suspend fun getLaunchDetails(flightNumber: Int): Flow<Resource<LaunchMinimal>> {
        return networkBoundFlow(
            dbFetcher = { getLaunchDetailsCached(flightNumber) },
            cacheValidator = { cached -> cached != null },
            apiFetcher = { getLaunchDetailsFromApi(flightNumber) },
            dataPersister = persistLaunchesUseCase::persistLaunch
        )
    }

    private suspend fun getLaunchDetailsCached(flightNumber: Int) = withContext(Dispatchers.IO) {
        launchesDao.getLaunch(flightNumber)
    }

    private suspend fun getLaunchDetailsFromApi(flightNumber: Int) = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchesService.getLaunch(flightNumber).await()
        }
    }
}