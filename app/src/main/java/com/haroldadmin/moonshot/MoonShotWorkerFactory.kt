package com.haroldadmin.moonshot

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import com.haroldadmin.moonshot.notifications.workers.ScheduleWorker
import com.haroldadmin.moonshot.sync.SyncWorker
import com.haroldadmin.moonshot.utils.log
import com.haroldadmin.moonshotRepository.SyncResourcesUseCase
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import javax.inject.Inject

class MoonShotWorkerFactory @Inject constructor(
    private val syncResourcesUseCase: SyncResourcesUseCase,
    private val nextLaunchUseCase: GetNextLaunchUseCase,
    private val launchNotificationsManager: LaunchNotificationsManager
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        return try {
            when (Class.forName(workerClassName)) {
                SyncWorker::class.java -> SyncWorker(
                    appContext,
                    workerParameters,
                    syncResourcesUseCase
                )
                ScheduleWorker::class.java -> ScheduleWorker(
                    appContext,
                    workerParameters,
                    nextLaunchUseCase,
                    launchNotificationsManager
                )
                else -> {
                    log(
                        """
                        Unknown worker class requested.
                        Class: $workerClassName
                        Parameters: $workerParameters
                        Context: $appContext
                        """.trimIndent()
                    )
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}