package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.moonshot.core.AppDispatchers
import com.haroldadmin.moonshot.database.LaunchDao
import com.haroldadmin.moonshotRepository.mappers.toDbLaunch
import com.haroldadmin.spacex_api_wrapper.launches.Launch
import com.haroldadmin.moonshot.models.launch.Launch as DbLaunch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PersistLaunchesUseCase @Inject constructor(
    private val launchesDao: LaunchDao,
    private val appDispatchers: AppDispatchers
) {

    suspend fun persistLaunch(apiLaunch: Launch) = withContext(appDispatchers.IO) {
        launchesDao.save(apiLaunch.toDbLaunch())
    }

    suspend fun persistLaunch(dbLaunch: DbLaunch) = withContext(appDispatchers.IO) {
        launchesDao.save(dbLaunch)
    }

    suspend fun persistLaunches(apiLaunches: List<Launch>, shouldSynchronize: Boolean = false) {
        withContext(appDispatchers.Default) {

            val dbLaunches = apiLaunches.map { it.toDbLaunch() }

            withContext(appDispatchers.IO) {
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
    ) = withContext(appDispatchers.IO) {
        if (shouldSynchronize) {
            launchesDao.synchronizeBlocking(dbLaunches)
        } else {
            launchesDao.saveAll(dbLaunches)
        }
    }
}