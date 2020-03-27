package com.haroldadmin.moonshotRepository.rocket

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.AppDispatchers
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.RocketsDao
import com.haroldadmin.moonshot.models.Rocket
import com.haroldadmin.moonshotRepository.SingleFetchNetworkBoundResource
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundResourceLazy
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import com.haroldadmin.spacex_api_wrapper.rocket.Rocket as ApiRocket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAllRocketsUseCase @Inject constructor(
    private val rocketsDao: RocketsDao,
    private val rocketsService: RocketsService,
    private val persistRocketsUseCase: PersistRocketsUseCase,
    private val appDispatchers: AppDispatchers
) {

    private val defaultLimit = 10
    private val defaultOffset = 0

    @ExperimentalCoroutinesApi
    private val allRocketsResource: SingleFetchNetworkBoundResource<List<Rocket>, List<ApiRocket>, ErrorResponse> by singleFetchNetworkBoundResourceLazy(
        dbFetcher = { _, limit, offset -> getAllRocketsCached(limit, offset) },
        cacheValidator = { cached -> !cached.isNullOrEmpty() },
        apiFetcher = { getAllRocketsFromApi() },
        dataPersister = { rockets -> persistRocketsUseCase.persistRockets(rockets) }
    )

    @ExperimentalCoroutinesApi
    fun getAllRockets(
        limit: Int = defaultLimit,
        offset: Int = defaultOffset
    ): Flow<Resource<List<Rocket>>> {
        allRocketsResource.updateParams(limit, offset)
        return allRocketsResource.flow()
    }

    @ExperimentalCoroutinesApi
    suspend fun sync(): Resource<Unit> = when (val apiRockets = getAllRocketsFromApi()) {
        is NetworkResponse.Success -> {
            persistRocketsUseCase.persistRockets(apiRockets.body, shouldSynchronize = true)
            Resource.Success(Unit)
        }
        else -> Resource.Error(Unit, null)
    }

    private suspend fun getAllRocketsCached(limit: Int, offset: Int) = withContext(appDispatchers.IO) {
        rocketsDao.all(limit, offset)
    }

    private suspend fun getAllRocketsFromApi() = withContext(appDispatchers.IO) {
        executeWithRetry {
            rocketsService.getAllRockets().await()
        }
    }
}