package com.haroldadmin.moonshotRepository.rocket

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.AppDispatchers
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.pairOf
import com.haroldadmin.moonshot.database.RocketsDao
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshotRepository.SingleFetchNetworkBoundResource
import com.haroldadmin.moonshotRepository.launch.PersistLaunchesUseCase
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundResourceLazy
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import com.haroldadmin.spacex_api_wrapper.launches.Launch as ApiLaunch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetLaunchesForRocketUseCase @Inject constructor(
    private val rocketsDao: RocketsDao,
    private val persistLaunchesUseCase: PersistLaunchesUseCase,
    private val launchesService: LaunchesService,
    private val appDispatchers: AppDispatchers
) {

    private val defaultLimit = 10
    private val defaultOffset = 0
    private var rocketId = ""

    @ExperimentalCoroutinesApi
    private val launchesForRocketResource: SingleFetchNetworkBoundResource<List<Launch>, List<ApiLaunch>, ErrorResponse> by singleFetchNetworkBoundResourceLazy(
        initialParams = ::initialParams,
        dbFetcher = { _, limit, offset -> getLaunchesForRocketCached(rocketId, limit, offset) },
        cacheValidator = { cached -> !cached.isNullOrEmpty() },
        apiFetcher = { getLaunchesForRocketFromApi() },
        dataPersister = { launches -> persistLaunchesUseCase.persistLaunches(launches) }
    )

    @ExperimentalCoroutinesApi
    fun getLaunchesForRocket(
        rocketId: String,
        limit: Int = 10,
        offset: Int = 0
    ): Flow<Resource<List<Launch>>> {
        this.rocketId = rocketId
        launchesForRocketResource.updateParams(limit, offset)
        return launchesForRocketResource.flow()
    }

    private suspend fun getLaunchesForRocketCached(
        rocketId: String,
        limit: Int,
        offset: Int
    ) = withContext(appDispatchers.IO) {
        rocketsDao.launchesForRocket(rocketId, limit, offset)
    }

    private suspend fun getLaunchesForRocketFromApi() = withContext(appDispatchers.IO) {
        executeWithRetry {
            launchesService.getAllLaunches().await()
        }
    }

    private fun initialParams() = pairOf(defaultLimit, defaultOffset)
}
