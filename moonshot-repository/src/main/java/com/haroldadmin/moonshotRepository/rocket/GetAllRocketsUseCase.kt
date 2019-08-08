package com.haroldadmin.moonshotRepository.rocket

import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.rocket.RocketsDao
import com.haroldadmin.moonshot.models.rocket.RocketMinimal
import com.haroldadmin.moonshotRepository.networkBoundFlow
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetAllRocketsUseCase(
    rocketsDao: RocketsDao,
    rocketsService: RocketsService
) : RocketsUseCase(rocketsDao, rocketsService) {

    suspend fun getAllRockets(): Flow<Resource<List<RocketMinimal>>> {
        return networkBoundFlow(
            dbFetcher = { getAllRocketsCached() },
            cacheValidator = { cached -> !cached.isNullOrEmpty() },
            apiFetcher = { getAllRocketsFromApi() },
            dataPersister = this::persistApiRockets
        )
    }

    private suspend fun getAllRocketsCached() = withContext(Dispatchers.IO) {
        rocketsDao.getAllRockets()
    }

    private suspend fun getAllRocketsFromApi() = withContext(Dispatchers.IO) {
        executeWithRetry {
            rocketsService.getAllRockets().await()
        }
    }
}