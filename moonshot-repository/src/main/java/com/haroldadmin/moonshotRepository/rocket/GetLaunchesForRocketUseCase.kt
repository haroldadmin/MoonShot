package com.haroldadmin.moonshotRepository.rocket

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.RocketsDao
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshotRepository.SingleFetchNetworkBoundResource
import com.haroldadmin.moonshotRepository.launch.PersistLaunchesUseCase
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundResource
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import com.haroldadmin.spacex_api_wrapper.launches.Launch as ApiLaunch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetLaunchesForRocketUseCase(
    private val rocketsDao: RocketsDao,
    private val persistLaunchesUseCase: PersistLaunchesUseCase,
    private val launchesService: LaunchesService
) {

    private lateinit var launchesForRocketResource: SingleFetchNetworkBoundResource<List<Launch>, List<ApiLaunch>, ErrorResponse>

    @ExperimentalCoroutinesApi
    fun getLaunchesForRocket(
        rocketId: String,
        limit: Int = 10,
        offset: Int = 0
    ): Flow<Resource<List<Launch>>> {
        if (!::launchesForRocketResource.isInitialized) {
            launchesForRocketResource = singleFetchNetworkBoundResource(
                dbFetcher = { getLaunchesForRocketCached(rocketId, limit, offset) },
                cacheValidator = { cached -> !cached.isNullOrEmpty() },
                apiFetcher = { getLaunchesForRocketFromApi() },
                dataPersister = { launches -> persistLaunchesUseCase.persistLaunches(launches) }
            )
        }
        return launchesForRocketResource.flow()
    }

    private suspend fun getLaunchesForRocketCached(
        rocketId: String,
        limit: Int,
        offset: Int
    ) = withContext(Dispatchers.IO) {
        rocketsDao.launchesForRocket(rocketId, limit, offset)
    }

    private suspend fun getLaunchesForRocketFromApi() = withContext(Dispatchers.IO) {
        executeWithRetry {
            launchesService.getAllLaunches().await()
        }
    }
}
