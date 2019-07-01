package com.haroldadmin.moonshot

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import com.haroldadmin.moonshot.notifications.NotificationSchedulingWorker
import com.haroldadmin.moonshot.sync.SyncWorker
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import org.koin.core.Koin

class MoonShotWorkerFactory(
    private val koin: Koin
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {

            SyncWorker::class.java.name -> SyncWorker(
                appContext = appContext,
                params = workerParameters,
                launchesRepository = koin.get<LaunchesRepository>()
            )

            NotificationSchedulingWorker::class.java.name -> NotificationSchedulingWorker(
                appContext = appContext,
                params = workerParameters,
                launchesRepository = koin.get<LaunchesRepository>(),
                launchNotificationsManager = koin.get<LaunchNotificationsManager>(),
                settings = PreferenceManager.getDefaultSharedPreferences(appContext)
            )

            else -> throw IllegalArgumentException("Unknown worker requested: $workerClassName")
        }
    }
}