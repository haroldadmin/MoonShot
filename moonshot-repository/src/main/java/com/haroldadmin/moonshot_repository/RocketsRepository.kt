package com.haroldadmin.moonshot_repository

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.safe
import com.haroldadmin.moonshot.database.rocket.RocketsDao
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RocketsRepository (
    private val rocketsService: RocketsService,
    private val rocketsDao: RocketsDao
) {

    suspend fun getAllRockets() = withContext(Dispatchers.IO) {

        val rockets = executeWithRetry { rocketsService.getAllRockets().await() }

        when (rockets) {
            is NetworkResponse.Success -> {
                rocketsDao.saveRockets(rockets.body.map {  })
            }
            is NetworkResponse.ServerError -> TODO()
            is NetworkResponse.NetworkError -> TODO()
        }.safe

    }

}