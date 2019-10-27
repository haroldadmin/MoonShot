package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.moonshot.database.LaunchDao
import com.haroldadmin.moonshotRepository.mappers.toDbLaunch
import com.haroldadmin.spacex_api_wrapper.launches.Launch
import com.haroldadmin.moonshot.models.launch.Launch as DbLaunch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersistLaunchesUseCase(private val launchesDao: LaunchDao) {

    suspend fun persistLaunch(apiLaunch: Launch) = withContext(Dispatchers.IO) {
        launchesDao.save(apiLaunch.toDbLaunch())
    }

    suspend fun persistLaunch(dbLaunch: DbLaunch) = withContext(Dispatchers.IO) {
        launchesDao.save(dbLaunch)
    }

    suspend fun persistLaunches(apiLaunches: List<Launch>, shouldSynchronize: Boolean = false) {
        withContext(Dispatchers.Default) {

            val dbLaunches = apiLaunches.map { it.toDbLaunch() }

            withContext(Dispatchers.IO) {
                if (shouldSynchronize) {
                    launchesDao.synchronizeBlocking(dbLaunches)
                } else {
                    launchesDao.saveAll(dbLaunches)
                }
            }
        }
    }

    @JvmName("persistDbLaunches")
    suspend fun persistLaunches(
        dbLaunches: List<DbLaunch>,
        shouldSynchronize: Boolean = false
    ) = withContext(Dispatchers.IO) {
        if (shouldSynchronize) {
            launchesDao.synchronizeBlocking(dbLaunches)
        } else {
            launchesDao.saveAll(dbLaunches)
        }
    }
}