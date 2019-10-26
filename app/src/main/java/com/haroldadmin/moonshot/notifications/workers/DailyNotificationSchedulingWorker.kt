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
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase

class DailyNotificationSchedulingWorker(
    appContext: Context,
    params: WorkerParameters,
    private val launchesRepository: GetNextLaunchUseCase,
    private val launchNotificationsManager: LaunchNotificationsManager,
    private val settings: SharedPreferences
) : CoroutineWorker(appContext, params) {

    companion object {
        const val NAME = "notification-scheduling-worker"
    }

    override suspend fun doWork(): Result {

//        val justBeforeLaunchNotificationsEnabled =
//            settings.getBoolean(KEY_JUST_BEFORE_LAUNCH_NOTIFICATIONS_SETTING, true)
//
//        val dayBeforeLaunchNotificationsEnabled =
//            settings.getBoolean(KEY_DAY_BEFORE_LAUNCH_NOTIFICATIONS_SETTING, true)
//
//        if (justBeforeLaunchNotificationsEnabled)
//            scheduleJustBeforeLaunchNotification()
//
//        if (dayBeforeLaunchNotificationsEnabled)
//            scheduleDayBeforeLaunchNotification()
//
        return Result.success()
    }
//
//    private suspend fun scheduleJustBeforeLaunchNotification() = launchesRepository
//        .getNextLaunchCached()
//        .takeIf { it != null }
//        ?.let { launchNotificationsManager.schedule(JustBeforeLaunch, it.launchDate.time) }
//        ?: log("Could not get next launch from database. Not scheduling notification")
//
//    private suspend fun scheduleDayBeforeLaunchNotification() = launchesRepository
//        .getNextLaunchCached()
//        .takeIf { it != null }
//        ?.let { launchNotificationsManager.schedule(DayBeforeLaunch, it.launchDate.time) }
//        ?: log("Could not get next launch from database. Not scheduling notification")
}
