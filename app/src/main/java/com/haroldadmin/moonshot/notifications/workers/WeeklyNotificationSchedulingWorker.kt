package com.haroldadmin.moonshot.notifications.workers

import android.content.Context
import android.content.SharedPreferences
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import com.haroldadmin.moonshot.notifications.WeekBeforeLaunch
import com.haroldadmin.moonshot.utils.log
import com.haroldadmin.moonshotRepository.launch.GetLaunchesUseCase
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate

class WeeklyNotificationSchedulingWorker(
    appContext: Context,
    params: WorkerParameters,
    private val launchesRepository: GetLaunchesUseCase,
    private val launchNotificationsManager: LaunchNotificationsManager,
    private val settings: SharedPreferences
) : CoroutineWorker(appContext, params) {

    companion object {
        const val NAME = "week-before-launch-notification-scheduling-worker"
    }

    override suspend fun doWork(): Result {

        val weekBeforeLaunchNotificationsEnabled =
            settings.getBoolean(LaunchNotificationsManager.KEY_WEEK_BEFORE_LAUNCH_NOTIFICATIONS_SETTING, true)

        if (weekBeforeLaunchNotificationsEnabled) {
            log("Scheduling week before launch notifications")
            schedule()
        }

        return Result.success()
    }

    private suspend fun schedule() {
//        val start = LocalDate.now(DateTimeZone.UTC).toDateTimeAtStartOfDay()
//        val end = start.plusDays(7)
//
//        launchesRepository
//            .getLaunches(
//                from = start.millis,
//                to = end.millis,
//                limit = 5
//            )
//            .takeIf { it.isNotEmpty() }
//            ?.let {
//                launchNotificationsManager.schedule(WeekBeforeLaunch, start.millis)
//            }
//            ?: log("There are no launches this week, not scheduling notifications")
    }
}