package com.haroldadmin.moonshotRepository.rocket

import com.haroldadmin.moonshot.database.RocketsDao
import com.haroldadmin.moonshot.models.Rocket as DbRocket
import com.haroldadmin.moonshotRepository.mappers.toDbRocket
import com.haroldadmin.spacex_api_wrapper.rocket.Rocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PersistRocketsUseCase @Inject constructor(private val rocketsDao: RocketsDao) {

    suspend fun persistRocket(apiRocket: Rocket) = withContext(Dispatchers.IO) {
        val dbRocket = apiRocket.toDbRocket()
        rocketsDao.save(dbRocket)
    }

    suspend fun persistRocket(dbRocket: DbRocket) = withContext(Dispatchers.IO) {
        rocketsDao.save(dbRocket)
    }

    suspend fun persistRockets(
        apiRockets: List<Rocket>,
        shouldSynchronize: Boolean = false
    ) = withContext(Dispatchers.Default) {
        val dbRockets = apiRockets.map { it.toDbRocket() }

        withContext(Dispatchers.IO) {
            if (shouldSynchronize) {
                rocketsDao.synchronizeBlocking(dbRockets)
            } else {
                rocketsDao.saveAll(dbRockets)
            }
        }
    }

    @JvmName("persistDbRockets")
    suspend fun persistRockets(
        dbRockets: List<DbRocket>,
        shouldSynchronize: Boolean = false
    ) = withContext(Dispatchers.IO) {
        if (shouldSynchronize) {
            rocketsDao.synchronizeBlocking(dbRockets)
        } else {
            rocketsDao.saveAll(dbRockets)
        }
    }
}