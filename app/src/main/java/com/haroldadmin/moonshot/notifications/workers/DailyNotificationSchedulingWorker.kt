package com.haroldadmin.moonshot.notifications.workers

import android.content.Context
import android.content.SharedPreferences
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.haroldadmin.moonshot.notifications.DayBeforeLaunch
import com.haroldadmin.moonshot.notifications.JustBeforeLaunch
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager.Companion.KEY_DAY_BEFORE_LAUNCH_NOTIFICATIONS_SETTING
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager.Companion.KEY_JUST_BEFORE_LAUNCH_NOTIFICATIONS_SETTING
import com.haroldadmin.moonshot.utils.log
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate

class DailyNotificationSchedulingWorker(
    appContext: Context,
    params: WorkerParameters,
    private val launchesRepository: LaunchesRepository,
    private val launchNotificationsManager: LaunchNotificationsManager,
    private val settings: SharedPreferences
) : CoroutineWorker(appContext, params) {

    companion object {
        const val NAME = "notification-scheduling-worker"
    }

    override suspend fun doWork(): Result {

        val justBeforeLaunchNotificationsEnabled =
            settings.getBoolean(KEY_JUST_BEFORE_LAUNCH_NOTIFICATIONS_SETTING, true)

        val dayBeforeLaunchNotificationsEnabled =
            settings.getBoolean(KEY_DAY_BEFORE_LAUNCH_NOTIFICATIONS_SETTING, true)

        if (justBeforeLaunchNotificationsEnabled)
            scheduleJustBeforeLaunchNotification()

        if (dayBeforeLaunchNotificationsEnabled)
            scheduleDayBeforeLaunchNotification()

        return Result.success()
    }

    private suspend fun scheduleJustBeforeLaunchNotification() {
        val currentTime = LocalDate.now(DateTimeZone.UTC).toDateTimeAtStartOfDay().millis
        val nextLaunch = launchesRepository.getNextFullLaunchFromDatabase(currentTime) ?: run {
            log("Could not get next launch from database. Not scheduling notification")
            return
        }
        launchNotificationsManager.schedule(
            JustBeforeLaunch,
            nextLaunch.launchDate.time
        )
    }

    private suspend fun scheduleDayBeforeLaunchNotification() {
        val currentTime = LocalDate.now(DateTimeZone.UTC).toDateTimeAtStartOfDay().millis
        val nextLaunch = launchesRepository.getNextFullLaunchFromDatabase(currentTime) ?: run {
            log("Could not get next launch from database. Not scheduling notification")
            return
        }

        launchNotificationsManager.schedule(
            DayBeforeLaunch,
            nextLaunch.launchDate.time
        )
    }
}
