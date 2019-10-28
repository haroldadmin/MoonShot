package com.haroldadmin.moonshotRepository.rocket

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.RocketsDao
import com.haroldadmin.moonshot.models.Rocket
import com.haroldadmin.moonshotRepository.SingleFetchNetworkBoundResource
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundFlow
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundResource
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import com.haroldadmin.spacex_api_wrapper.rocket.Rocket as ApiRocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetAllRocketsUseCase(
    private val rocketsDao: RocketsDao,
    private val rocketsService: RocketsService,
    private val persistRocketsUseCase: PersistRocketsUseCase
) {

    private lateinit var allRocketsResource: SingleFetchNetworkBoundResource<List<Rocket>, List<ApiRocket>, ErrorResponse>

    @ExperimentalCoroutinesApi
    fun getAllRockets(
        limit: Int = 10,
        offset: Int = 0
    ): Flow<Resource<List<Rocket>>> {
        if (!::allRocketsResource.isInitialized) {
            allRocketsResource = singleFetchNetworkBoundResource(
                dbFetcher = { getAllRocketsCached(limit, offset) },
                cacheValidator = { cached -> !cached.isNullOrEmpty() },
                apiFetcher = { getAllRocketsFromApi() },
                dataPersister = { rockets -> persistRocketsUseCase.persistRockets(rockets) }
            )
        }
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

    private suspend fun getAllRocketsCached(limit: Int, offset: Int) = withContext(Dispatchers.IO) {
        rocketsDao.all(limit, offset)
    }

    private suspend fun getAllRocketsFromApi() = withContext(Dispatchers.IO) {
        executeWithRetry {
            rocketsService.getAllRockets().await()
        }
    }
}