package com.haroldadmin.moonshotRepository.rocket

import com.haroldadmin.moonshot.database.rocket.RocketsDao
import com.haroldadmin.moonshotRepository.mappers.toDbPayloadWeight
import com.haroldadmin.moonshotRepository.mappers.toDbRocket
import com.haroldadmin.spacex_api_wrapper.rocket.Rocket
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersistRocketsUseCase(private val rocketsDao: RocketsDao) {

    suspend fun persistApiRocket(apiRocket: Rocket) =
        withContext(Dispatchers.Default) {
            val dbRocket = apiRocket.toDbRocket()
            val dbPayloadWeights = apiRocket
                .payloadWeights
                .map { it.toDbPayloadWeight(apiRocket.rocketId) }

            withContext(Dispatchers.IO) {
                rocketsDao.saveRocketWithPayloadWeights(dbRocket, dbPayloadWeights)
            }
        }

    suspend fun persistApiRockets(apiRockets: List<Rocket>) =
        withContext(Dispatchers.Default) {
            val dbRockets = apiRockets.map { it.toDbRocket() }
            val dbPayloadWeights = apiRockets
                .flatMap { apiRocket ->
                    apiRocket
                        .payloadWeights
                        .map { it.toDbPayloadWeight(apiRocket.rocketId) }
                }

            withContext(Dispatchers.IO) {
                rocketsDao.saveRocketsWithPayloadWeights(dbRockets, dbPayloadWeights)
            }
        }
}