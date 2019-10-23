package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshotRepository.networkBoundFlow
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundFlow
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetNextLaunchUseCase(
    private val launchesDao: LaunchDao,
    private val launchesService: LaunchesService,
    private val persistLaunchesUseCase: PersistLaunchesUseCase
) {

    suspend fun getNextLaunch(): Flow<Resource<LaunchMinimal>> {
        return singleFetchNetworkBoundFlow(
            dbFetcher = { getNextLaunchCached() },
            cacheValidator = { cachedData -> cachedData != null },
            apiFetcher = { getNextLaunchFromService() },
            dataPersister = persistLaunchesUseCase::persistLaunch
        )
    }

    suspend fun getNextLaunchCached() = withContext(Dispatchers.IO) {
        launchesDao.getNextLaunch()
    }

    private suspend fun getNextLaunchFromService() = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchesService.getNextLaunch().await()
        }
    }
}