package com.haroldadmin.moonshotRepository.rocket

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.rocket.RocketsDao
import com.haroldadmin.moonshot.models.rocket.RocketMinimal
import com.haroldadmin.moonshotRepository.singleFetchNetworkBoundFlow
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetRocketDetailsUseCase(
    private val rocketsDao: RocketsDao,
    private val rocketsService: RocketsService,
    private val persistRocketsUseCase: PersistRocketsUseCase
) {

    private suspend fun getCached(rocketId: String) = withContext(Dispatchers.IO) {
        rocketsDao.getRocket(rocketId)
    }

    private suspend fun getFromApi(rocketId: String) = withContext(Dispatchers.IO) {
        executeWithRetry {
            rocketsService.getRocket(rocketId).await()
        }
    }

    suspend fun getRocketDetails(rocketId: String): Flow<Resource<RocketMinimal>> {
        return singleFetchNetworkBoundFlow(
            dbFetcher = { getCached(rocketId) },
            cacheValidator = { cached -> cached != null },
            apiFetcher = { getFromApi(rocketId) },
            dataPersister = persistRocketsUseCase::persistApiRocket
        )
    }
}