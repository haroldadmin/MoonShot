package com.haroldadmin.moonshotRepository

import com.haroldadmin.moonshot.core.AppDispatchers
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.ApplicationInfo
import com.haroldadmin.moonshotRepository.applicationInfo.ApplicationInfoUseCase
import com.haroldadmin.moonshotRepository.launch.GetLaunchesUseCase
import com.haroldadmin.moonshotRepository.launchPad.GetLaunchPadUseCase
import com.haroldadmin.moonshotRepository.rocket.GetAllRocketsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class SyncResourcesUseCase @Inject constructor(
    private val launchesUseCase: GetLaunchesUseCase,
    private val rocketsUseCase: GetAllRocketsUseCase,
    private val launchpadsUseCase: GetLaunchPadUseCase,
    private val appInfoUseCase: ApplicationInfoUseCase,
    private val appDispatchers: AppDispatchers
) {

    @ExperimentalCoroutinesApi
    suspend fun sync(): Resource<Unit> = withContext(appDispatchers.IO) {

        val isSuccessful = listOf(launchesUseCase.sync(), rocketsUseCase.sync(), launchpadsUseCase.sync())
            .any { it is Resource.Error<*, *> }

        if (!isSuccessful) {
            Resource.Error(null, Unit)
        } else {
            logUpdate()
            Resource.Success(Unit)
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
