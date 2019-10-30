package com.haroldadmin.moonshotRepository.rocket

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.RocketsDao
import com.haroldadmin.moonshot.models.Rocket
import com.haroldadmin.moonshotRepository.SingleFetchNetworkBoundResource
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundResourceLazy
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import com.haroldadmin.spacex_api_wrapper.rocket.Rocket as ApiRocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRocketDetailsUseCase @Inject constructor(
    private val rocketsDao: RocketsDao,
    private val rocketsService: RocketsService,
    private val persistRocketsUseCase: PersistRocketsUseCase
) {

    private var rocketId: String = ""

    private val rocketDetailsResource: SingleFetchNetworkBoundResource<Rocket, ApiRocket, ErrorResponse> by singleFetchNetworkBoundResourceLazy(
        dbFetcher = { _, _, _ -> getCached(rocketId) },
        cacheValidator = { cached -> cached != null },
        apiFetcher = { getFromApi(rocketId) },
        dataPersister = persistRocketsUseCase::persistRocket
    )

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
        this.rocketId = rocketId
        return rocketDetailsResource.flow()
    }
}