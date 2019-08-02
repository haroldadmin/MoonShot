package com.haroldadmin.moonshot.notifications.workers

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
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

            DailyNotificationSchedulingWorker::class.java.name -> DailyNotificationSchedulingWorker(
                appContext = appContext,
                params = workerParameters,
                launchesRepository = koin.get<LaunchesRepository>(),
                launchNotificationsManager = koin.get<LaunchNotificationsManager>(),
                settings = PreferenceManager.getDefaultSharedPreferences(appContext)
            )

            WeeklyNotificationSchedulingWorker::class.java.name -> WeeklyNotificationSchedulingWorker(
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