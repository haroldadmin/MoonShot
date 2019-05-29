package com.haroldadmin.moonshot_repository.rocket

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.cnradapter.invoke
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.rocket.RocketsDao
import com.haroldadmin.moonshot.models.rocket.Rocket
import com.haroldadmin.moonshot_repository.mappers.toDbPayloadWeight
import com.haroldadmin.moonshot_repository.mappers.toDbRocket
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RocketsRepository(
    private val rocketsService: RocketsService,
    private val rocketsDao: RocketsDao
) {

    suspend fun getAllRockets(): Resource<List<Rocket>> = withContext(Dispatchers.IO) {

        when (val rockets = executeWithRetry { rocketsService.getAllRockets().await() }) {
            is NetworkResponse.Success -> {
                val apiRocket = rockets()!!
                val dbRockets = apiRocket.map { rocket ->
                    rocket.toDbRocket()
                }

                val dbPayloadWeights = apiRocket.flatMap { rocket ->
                    rocket.payloadWeights.map { it.toDbPayloadWeight(rocket.rockedId) }
                }

                rocketsDao.saveRocketsWithPayloadWeights(dbRockets, dbPayloadWeights)
                Resource.Success(rocketsDao.getAllRockets())
            }
            is NetworkResponse.ServerError -> {
                Resource.Error(rocketsDao.getAllRockets(), rockets.body)
            }
            is NetworkResponse.NetworkError -> {
                Resource.Error(rocketsDao.getAllRockets(), rockets.error)
            }
            else -> {
                Resource.Error(rocketsDao.getAllRockets(), null)
            }
        }
    }

    suspend fun getRocket(rocketId: String): Resource<Rocket> = withContext(Dispatchers.IO) {
        when (val rocket = executeWithRetry { rocketsService.getRocket(rocketId).await() }) {
            is NetworkResponse.Success -> {
                val apiRocket = rocket()!!
                val dbRocket = apiRocket.toDbRocket()
                val dbPayloadWeights = apiRocket.payloadWeights.map { it.toDbPayloadWeight(apiRocket.rockedId) }

                rocketsDao.saveRocketWithPayloadWeights(dbRocket, dbPayloadWeights)

                Resource.Success(rocketsDao.getRocket(rocketId))
            }
            is NetworkResponse.ServerError -> {
                Resource.Error(rocketsDao.getRocket(rocketId), rocket.body)
            }
            is NetworkResponse.NetworkError -> {
                Resource.Error(rocketsDao.getRocket(rocketId), rocket.error)
            }
            else -> Resource.Error(rocketsDao.getRocket(rocketId), null)
        }
    }

}