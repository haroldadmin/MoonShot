package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshotRepository.networkBoundFlow
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetNextLaunchUseCase(
    launchesDao: LaunchDao,
    launchesService: LaunchesService
): LaunchesUseCase(launchesDao, launchesService) {

    suspend fun getNextLaunch(timeAtStartOfDay: Long): Flow<Resource<LaunchMinimal>> {
        return networkBoundFlow(
            dbFetcher = { getNextLaunchCached(timeAtStartOfDay) },
            cacheValidator = { cachedData -> cachedData != null },
            apiFetcher = { getNextLaunchFromService() },
            dataPersister = this::persistLaunch
        )
    }

    private suspend fun getNextLaunchCached(timeAtStartOfDay: Long) = withContext(Dispatchers.IO) {
        launchesDao.getNextLaunch(timeAtStartOfDay)
    }

    private suspend fun getNextLaunchFromService() = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchesService.getNextLaunch().await()
        }
    }
}