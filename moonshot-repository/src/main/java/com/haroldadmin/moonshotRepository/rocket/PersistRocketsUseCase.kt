package com.haroldadmin.moonshotRepository.rocket

import com.haroldadmin.moonshot.database.RocketsDao
import com.haroldadmin.moonshotRepository.mappers.toDbRocket
import com.haroldadmin.spacex_api_wrapper.rocket.Rocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersistRocketsUseCase(private val rocketsDao: RocketsDao) {

    suspend fun persistApiRocket(apiRocket: Rocket) = withContext(Dispatchers.IO) {
        val dbRocket = apiRocket.toDbRocket()
        rocketsDao.save(dbRocket)
    }

    suspend fun persistApiRockets(apiRockets: List<Rocket>, shouldSynchronize: Boolean = false) = withContext(Dispatchers.Default) {
        val dbRockets = apiRockets.map { it.toDbRocket() }

        withContext(Dispatchers.IO) {
            if (shouldSynchronize) {
                rocketsDao.synchronizeBlocking(dbRockets)
            } else {
                rocketsDao.saveAll(dbRockets)
            }
        }
    }
}