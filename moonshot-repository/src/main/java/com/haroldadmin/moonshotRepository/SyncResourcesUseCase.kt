package com.haroldadmin.moonshotRepository

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshot.models.ApplicationInfo
import com.haroldadmin.moonshotRepository.applicationInfo.ApplicationInfoUseCase
import com.haroldadmin.moonshotRepository.launch.GetLaunchesUseCase
import com.haroldadmin.moonshotRepository.launch.LaunchType
import com.haroldadmin.moonshotRepository.launch.PersistLaunchesUseCase
import com.haroldadmin.moonshotRepository.launchPad.GetLaunchPadUseCase
import com.haroldadmin.moonshotRepository.launchPad.PersistLaunchPadUseCase
import com.haroldadmin.moonshotRepository.rocket.GetAllRocketsUseCase
import com.haroldadmin.moonshotRepository.rocket.PersistRocketsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.Date

class SyncResourcesUseCase(
    private val launchesUseCase: GetLaunchesUseCase,
    private val persistLaunchesUseCase: PersistLaunchesUseCase,
    private val rocketsUseCase: GetAllRocketsUseCase,
    private val persistRocketsUseCase: PersistRocketsUseCase,
    private val launchpadsUseCase: GetLaunchPadUseCase,
    private val persistLaunchPadUseCase: PersistLaunchPadUseCase,
    private val appInfoUseCase: ApplicationInfoUseCase
) {

    @ExperimentalCoroutinesApi
    suspend fun sync(): Resource<Unit> = withContext(Dispatchers.IO) {

        val launches = launchesUseCase.getLaunches(LaunchType.All, limit = Int.MAX_VALUE).first()
        val rockets = rocketsUseCase.getAllRockets(limit = Int.MAX_VALUE).first()
        val launchPads = launchpadsUseCase.getAllLaunchPads(limit = Int.MAX_VALUE).first()

        if (launches is Resource.Error<*, *> || rockets is Resource.Error<*, *> || launchPads is Resource.Error<*, *>) {
            Resource.Error(null, Unit)
        }

        try {
            launches()?.let {
                persistLaunchesUseCase.persistLaunches(it, shouldSynchronize = true)
            }

            rockets()?.let {
                persistRocketsUseCase.persistRockets(it, shouldSynchronize = true)
            }

            launchPads()?.let {
                persistLaunchPadUseCase.persistLaunchPads(it, shouldSynchronize = true)
            }

            logUpdate()

            Resource.Success(Unit)
        } catch (ex: Exception) {
            Resource.Error(null, Unit)
        }
    }

    private suspend fun logUpdate() {
        val syncTime = Date().time
        val appInfo = appInfoUseCase.getApplicationInfo()

        if (appInfo == null) {
            appInfoUseCase.save(ApplicationInfo(syncTime))
        } else {
            appInfoUseCase.update(appInfo)
        }
    }
}
