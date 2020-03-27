package com.haroldadmin.moonshotRepository.rocket

import com.haroldadmin.moonshot.core.AppDispatchers
import com.haroldadmin.moonshot.database.RocketsDao
import com.haroldadmin.moonshot.models.Rocket as DbRocket
import com.haroldadmin.moonshotRepository.mappers.toDbRocket
import com.haroldadmin.spacex_api_wrapper.rocket.Rocket
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PersistRocketsUseCase @Inject constructor(
    private val rocketsDao: RocketsDao,
    private val appDispatchers: AppDispatchers
) {

    suspend fun persistRocket(apiRocket: Rocket) = withContext(appDispatchers.IO) {
        val dbRocket = apiRocket.toDbRocket()
        rocketsDao.save(dbRocket)
    }

    suspend fun persistRocket(dbRocket: DbRocket) = withContext(appDispatchers.IO) {
        rocketsDao.save(dbRocket)
    }

    suspend fun persistRockets(
        apiRockets: List<Rocket>,
        shouldSynchronize: Boolean = false
    ) = withContext(appDispatchers.Default) {
        val dbRockets = apiRockets.map { it.toDbRocket() }

        withContext(appDispatchers.IO) {
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
    ) = withContext(appDispatchers.IO) {
        if (shouldSynchronize) {
            rocketsDao.synchronizeBlocking(dbRockets)
        } else {
            rocketsDao.saveAll(dbRockets)
        }
    }
}