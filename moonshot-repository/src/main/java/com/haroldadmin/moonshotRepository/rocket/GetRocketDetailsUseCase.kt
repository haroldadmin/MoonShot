package com.haroldadmin.moonshotRepository.rocket

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.RocketsDao
import com.haroldadmin.moonshot.models.Rocket
import com.haroldadmin.moonshotRepository.SingleFetchNetworkBoundResource
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundResource
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import com.haroldadmin.spacex_api_wrapper.rocket.Rocket as ApiRocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetRocketDetailsUseCase(
    private val rocketsDao: RocketsDao,
    private val rocketsService: RocketsService,
    private val persistRocketsUseCase: PersistRocketsUseCase
) {

    private lateinit var rocketDetailsResource: SingleFetchNetworkBoundResource<Rocket, ApiRocket, ErrorResponse>

    private suspend fun getCached(rocketId: String) = withContext(Dispatchers.IO) {
        rocketsDao.one(rocketId)
    }

    private suspend fun getFromApi(rocketId: String) = withContext(Dispatchers.IO) {
        executeWithRetry {
            rocketsService.getRocket(rocketId).await()
        }
    }

    @ExperimentalCoroutinesApi
    fun getRocketDetails(rocketId: String): Flow<Resource<Rocket>> {
        if (!::rocketDetailsResource.isInitialized) {
            rocketDetailsResource = singleFetchNetworkBoundResource(
                dbFetcher = { getCached(rocketId) },
                cacheValidator = { cached -> cached != null },
                apiFetcher = { getFromApi(rocketId) },
                dataPersister = persistRocketsUseCase::persistRocket
            )
        }

        return rocketDetailsResource.flow()
    }
}