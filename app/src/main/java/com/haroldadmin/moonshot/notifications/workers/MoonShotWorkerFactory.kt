package com.haroldadmin.moonshot.notifications.workers

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.haroldadmin.moonshot.sync.SyncWorker
import com.haroldadmin.moonshot.utils.log
import org.koin.core.KoinComponent

class MoonShotWorkerFactory: WorkerFactory(), KoinComponent {

    private val koinRef = getKoin()

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        return try {
            when (Class.forName(workerClassName)) {

                SyncWorker::class.java -> SyncWorker(
                    appContext = appContext,
                    params = workerParameters,
                    launchesRepository = koinRef.get()
                )

                DailyNotificationSchedulingWorker::class.java -> DailyNotificationSchedulingWorker(
                    appContext = appContext,
                    params = workerParameters,
                    launchesRepository = koinRef.get(),
                    launchNotificationsManager = koinRef.get(),
                    settings = PreferenceManager.getDefaultSharedPreferences(appContext)
                )

                WeeklyNotificationSchedulingWorker::class.java -> WeeklyNotificationSchedulingWorker(
                    appContext = appContext,
                    params = workerParameters,
                    launchesRepository = koinRef.get(),
                    launchNotificationsManager = koinRef.get(),
                    settings = PreferenceManager.getDefaultSharedPreferences(appContext)
                )
                else -> {
                    log("""
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