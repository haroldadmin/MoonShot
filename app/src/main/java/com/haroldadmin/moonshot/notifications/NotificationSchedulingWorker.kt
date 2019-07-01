package com.haroldadmin.moonshot.notifications

import android.content.Context
import android.content.SharedPreferences
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager.Companion.KEY_DAY_BEFORE_LAUNCH_NOTIFICATIONS_SETTING
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager.Companion.KEY_JUST_BEFORE_LAUNCH_NOTIFICATIONS_SETTING
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager.Companion.KEY_WEEK_BEFORE_LAUNCH_NOTIFICATIONS_SETTING
import com.haroldadmin.moonshot.utils.log
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import java.util.Date

class NotificationSchedulingWorker(
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

        val weekBeforeLaunchNotificationsEnabled =
            settings.getBoolean(KEY_WEEK_BEFORE_LAUNCH_NOTIFICATIONS_SETTING, true)

        if (!justBeforeLaunchNotificationsEnabled &&
            !dayBeforeLaunchNotificationsEnabled &&
            !weekBeforeLaunchNotificationsEnabled
        ) {
            log("No notifications are enabled, returning Failure result")
            return Result.failure()
        }

        if (justBeforeLaunchNotificationsEnabled)
            scheduleJustBeforeLaunchNotification()

        if (dayBeforeLaunchNotificationsEnabled)
            scheduleDayBeforeLaunchNotification()

        if (weekBeforeLaunchNotificationsEnabled)
            scheduleWeekBeforeLaunchNotifications()

        return Result.success()
    }

    private suspend fun scheduleJustBeforeLaunchNotification() {
        val currentTime = LocalDate.now(DateTimeZone.UTC).toDateTimeAtStartOfDay().millis
        val nextLaunch = launchesRepository.getNextLaunchFromDatabase(currentTime) ?: run {
            log("Could not get next launch from database. Not scheduling notification")
            return
        }
        launchNotificationsManager.schedule(
            LaunchNotification.JUST_BEFORE,
            nextLaunch.launchDate.time
        )
    }

    private suspend fun scheduleDayBeforeLaunchNotification() {
        val currentTime = LocalDate.now(DateTimeZone.UTC).toDateTimeAtStartOfDay().millis
        val nextLaunch = launchesRepository.getNextLaunchFromDatabase(currentTime) ?: run {
            log("Could not get next launch from database. Not scheduling notification")
            return
        }

        launchNotificationsManager.schedule(
            LaunchNotification.DAY_BEFORE,
            nextLaunch.launchDate.time
        )
    }

    private suspend fun scheduleWeekBeforeLaunchNotifications() {
        val start = LocalDate.now(DateTimeZone.UTC).toDateTimeAtStartOfDay()
        val end = start.plusDays(7)

        launchesRepository
            .getLaunchesInTimeRangeFromDatabase(
                start = start.millis,
                end = end.millis,
                limit = 5
            )
            .takeIf { it.isNotEmpty() }
            ?.let {
                launchNotificationsManager.schedule(LaunchNotification.WEEK_BEFORE, Date().time)
            }
            ?: log("There are no launches this week, not scheduling notifications")
    }
}
