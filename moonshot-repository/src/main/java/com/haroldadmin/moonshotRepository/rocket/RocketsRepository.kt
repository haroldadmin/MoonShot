package com.haroldadmin.moonshotRepository.rocket

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.cnradapter.invoke
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.rocket.RocketsDao
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.rocket.Rocket
import com.haroldadmin.moonshot.models.rocket.RocketMinimal
import com.haroldadmin.moonshotRepository.mappers.toDbPayloadWeight
import com.haroldadmin.moonshotRepository.mappers.toDbRocket
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class RocketsRepository(
    private val rocketsService: RocketsService,
    private val rocketsDao: RocketsDao
) {

    suspend fun flowAllRocketsMinimal() = flow<Resource<List<RocketMinimal>>> {
        emit(Resource.Loading)
        val dbRockets = rocketsDao.getAllRockets()
        if (dbRockets.isNotEmpty()) {
            emit(Resource.Success(dbRockets))
        }

        when (val networkResponse = executeWithRetry { rocketsService.getAllRockets().await() }) {
            is NetworkResponse.Success -> {
                val rockets = networkResponse.body.map { it.toDbRocket() }
                rocketsDao.saveAll(rockets)
                val savedRockets = rocketsDao.getAllRockets()
                if (savedRockets != dbRockets) {
                    emit(Resource.Success(savedRockets))
                }
            }
            is NetworkResponse.ServerError -> emit(Resource.Error(dbRockets, networkResponse.body))
            is NetworkResponse.NetworkError -> emit(Resource.Error(dbRockets, networkResponse.error))
        }
    }
        .flowOn(Dispatchers.IO)

    suspend fun flowRocketMinimal(rocketId: String) = flow<Resource<RocketMinimal>> {
        emit(Resource.Loading)
        val dbRocket = rocketsDao.getRocketMinimal(rocketId)
        dbRocket?.let { emit(Resource.Success(it)) }

        when (val rocketResponse = executeWithRetry { rocketsService.getRocket(rocketId).await() }) {
            is NetworkResponse.Success -> {
                rocketsDao.save(rocketResponse.body.toDbRocket())
                val savedRocket = rocketsDao.getRocketMinimal(rocketId)!!
                if (savedRocket != dbRocket) {
                    emit(Resource.Success(savedRocket))
                }
            }
            is NetworkResponse.ServerError -> emit(Resource.Error(dbRocket, rocketResponse.body))
            is NetworkResponse.NetworkError -> emit(Resource.Error(dbRocket, rocketResponse.error))
        }
    }
        .flowOn(Dispatchers.IO)

    suspend fun flowLaunchesForRocket(rocketId: String, timestamp: Long) = flow<Resource<List<LaunchMinimal>>> {
        emit(Resource.Loading)
        val launches = rocketsDao.getLaunchesForRocket(rocketId, timestamp)
        emit(Resource.Success(launches))
    }

   suspend fun getRocket(rocketId: String): Resource<Rocket> = withContext(Dispatchers.IO) {
        when (val rocket = executeWithRetry { rocketsService.getRocket(rocketId).await() }) {
            is NetworkResponse.Success -> {
                val apiRocket = rocket()!!
                val dbRocket = apiRocket.toDbRocket()
                val dbPayloadWeights = apiRocket.payloadWeights.map { it.toDbPayloadWeight(apiRocket.rocketId) }

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