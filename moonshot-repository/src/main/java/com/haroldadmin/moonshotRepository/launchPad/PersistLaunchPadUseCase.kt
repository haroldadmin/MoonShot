package com.haroldadmin.moonshotRepository.launchPad

import com.haroldadmin.moonshot.core.AppDispatchers
import com.haroldadmin.moonshot.database.LaunchPadDao
import com.haroldadmin.moonshot.models.LaunchPad as DbLaunchPad
import com.haroldadmin.moonshotRepository.mappers.toDbLaunchPad
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPad
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PersistLaunchPadUseCase @Inject constructor(
    private val launchPadDao: LaunchPadDao,
    private val appDispatchers: AppDispatchers
) {

    suspend fun persistLaunchPad(apiLaunchPad: LaunchPad) = withContext(appDispatchers.IO) {
        launchPadDao.save(apiLaunchPad.toDbLaunchPad())
    }

    suspend fun persistLaunchPad(dbLaunchPad: DbLaunchPad) = withContext(appDispatchers.IO) {
        launchPadDao.save(dbLaunchPad)
    }

    suspend fun persistLaunchPads(
        apiLaunchPads: List<LaunchPad>,
        shouldSynchronize: Boolean = false
    ) = withContext(appDispatchers.IO) {
        val dbLaunchPads = apiLaunchPads.map { it.toDbLaunchPad() }
        if (shouldSynchronize) {
            launchPadDao.synchronizeBlocking(dbLaunchPads)
        } else {
            launchPadDao.saveAll(dbLaunchPads)
        }
    }

    @JvmName("persistDbLaunchPads")
    suspend fun persistLaunchPads(
        dbLaunchPads: List<DbLaunchPad>,
        shouldSynchronize: Boolean = false
    ) = withContext(appDispatchers.IO) {
        if (shouldSynchronize) {
            launchPadDao.synchronizeBlocking(dbLaunchPads)
        } else {
            launchPadDao.saveAll(dbLaunchPads)
        }
    }
}