package com.haroldadmin.moonshotRepository.rocket

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.RocketsDao
import com.haroldadmin.moonshot.models.Rocket
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundFlow
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetAllRocketsUseCase(
    private val rocketsDao: RocketsDao,
    private val rocketsService: RocketsService,
    private val persistRocketsUseCase: PersistRocketsUseCase
) {

    @ExperimentalCoroutinesApi
    fun getAllRockets(
        limit: Int = 10,
        offset: Int = 0
    ): Flow<Resource<List<Rocket>>> {
        return singleFetchNetworkBoundFlow(
            dbFetcher = { getAllRocketsCached(limit, offset) },
            cacheValidator = { cached -> !cached.isNullOrEmpty() },
            apiFetcher = { getAllRocketsFromApi() },
            dataPersister = { rockets -> persistRocketsUseCase.persistApiRockets(rockets) }
        )
    }

    @ExperimentalCoroutinesApi
    suspend fun synchronize(): Resource<Unit> {
        val apiRockets = executeWithRetry {
            rocketsService.getAllRockets().await()
        }

        return when (apiRockets) {
            is NetworkResponse.Success -> {
                persistRocketsUseCase.persistApiRockets(apiRockets.body, shouldSynchronize = true)
                Resource.Success(Unit)
            }
            is NetworkResponse.ServerError -> Resource.Error(Unit, null)
            is NetworkResponse.NetworkError -> Resource.Error(Unit, null)
        }
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