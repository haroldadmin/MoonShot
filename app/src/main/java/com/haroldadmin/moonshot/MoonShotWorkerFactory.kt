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
import org.koin.core.KoinComponent

class MoonShotWorkerFactory : WorkerFactory(), KoinComponent {

    private val koinRef = getKoin()

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
                    koinRef.get<SyncResourcesUseCase>()
                )
                ScheduleWorker::class.java -> ScheduleWorker(
                    appContext,
                    workerParameters,
                    koinRef.get<GetNextLaunchUseCase>(),
                    koinRef.get<LaunchNotificationsManager>()
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