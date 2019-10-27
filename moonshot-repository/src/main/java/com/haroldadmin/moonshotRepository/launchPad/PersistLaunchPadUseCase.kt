package com.haroldadmin.moonshotRepository.launchPad

import com.haroldadmin.moonshot.database.LaunchPadDao
import com.haroldadmin.moonshot.models.LaunchPad as DbLaunchPad
import com.haroldadmin.moonshotRepository.mappers.toDbLaunchPad
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersistLaunchPadUseCase(private val launchPadDao: LaunchPadDao) {

    suspend fun persistLaunchPad(apiLaunchPad: LaunchPad) = withContext(Dispatchers.IO) {
        launchPadDao.save(apiLaunchPad.toDbLaunchPad())
    }

    suspend fun persistLaunchPad(dbLaunchPad: DbLaunchPad) = withContext(Dispatchers.IO) {
        launchPadDao.save(dbLaunchPad)
    }

    suspend fun persistLaunchPads(
        apiLaunchPads: List<LaunchPad>,
        shouldSynchronize: Boolean = false
    ) = withContext(Dispatchers.IO) {
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
    ) = withContext(Dispatchers.IO) {
        if (shouldSynchronize) {
            launchPadDao.synchronizeBlocking(dbLaunchPads)
        } else {
            launchPadDao.saveAll(dbLaunchPads)
        }
    }
}