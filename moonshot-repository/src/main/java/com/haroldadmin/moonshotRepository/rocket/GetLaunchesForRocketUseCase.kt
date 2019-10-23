package com.haroldadmin.moonshotRepository.rocket

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.rocket.RocketsDao
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshotRepository.launch.PersistLaunchesUseCase
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundFlow
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetLaunchesForRocketUseCase(
    private val rocketsDao: RocketsDao,
    private val persistLaunchesUseCase: PersistLaunchesUseCase,
    private val launchesService: LaunchesService
) {

    suspend fun getLaunchesForRocket(
        rocketId: String,
        currentTime: Long,
        limit: Int = 10
    ): Flow<Resource<List<LaunchMinimal>>> {
        return singleFetchNetworkBoundFlow(
            dbFetcher = { getLaunchesForRocketCached(rocketId, currentTime, limit) },
            cacheValidator = { cached -> !cached.isNullOrEmpty() },
            apiFetcher = { getLaunchesForRocketFromApi() },
            dataPersister = persistLaunchesUseCase::persistLaunches
        )
    }

    private suspend fun getLaunchesForRocketCached(
        rocketId: String,
        currentTime: Long,
        limit: Int
    ) = withContext(Dispatchers.IO) {
        rocketsDao.getLaunchesForRocket(rocketId, currentTime, limit)
    }

    private suspend fun getLaunchesForRocketFromApi() = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchesService.getAllLaunches().await()
        }
    }
}
