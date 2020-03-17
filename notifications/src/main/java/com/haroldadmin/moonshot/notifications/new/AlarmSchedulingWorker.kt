package com.haroldadmin.moonshot.notifications.new

import android.content.Context
import android.content.SharedPreferences
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.models.NotificationType
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.notifications.new.NotificationConstants.DayBeforeLaunch
import com.haroldadmin.moonshot.notifications.new.NotificationConstants.JustBeforeLaunch
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.joda.time.DateTime
import javax.inject.Inject
import javax.inject.Named

/**
 * Runs periodically at a fixed interval to retrieve launch information
 * and schedule alarms for their notifications.
 */
class AlarmSchedulingWorker @Inject constructor(
    appContext: Context,
    params: WorkerParameters,
    private val nextLaunchUseCase: GetNextLaunchUseCase,
    @Named("settings")
    private val settings: SharedPreferences,
    private val alarmScheduler: AlarmScheduler
) : CoroutineWorker(appContext, params) {

    @ExperimentalCoroutinesApi
    override suspend fun doWork(): Result {
        val nextLaunchRes = nextLaunchUseCase.getNextLaunch().last()

        if (nextLaunchRes !is Resource.Success) {
            return Result.retry()
        }

        scheduleNotifications(nextLaunchRes())
        return Result.success()
    }

    private fun scheduleNotifications(
        nextLaunch: Launch
    ) {
        val isJustBeforeLaunchNotifEnabled = settings.getBoolean(JustBeforeLaunch.settingsKey, true)
        val isDayBeforeLaunchNotifEnabled = settings.getBoolean(DayBeforeLaunch.settingsKey, true)

        if (isJustBeforeLaunchNotifEnabled) {
            alarmScheduler.schedule(NotificationType.JustBeforeLaunch, nextLaunch.launchDateUtc.time)
        }

        if (isDayBeforeLaunchNotifEnabled) {
            val notifTime = DateTime(nextLaunch.launchDateUtc).minusDays(1)
            alarmScheduler.schedule(NotificationType.DayBeforeLaunch, notifTime.millis)
        }
    }

    companion object {
        const val name: String = "com.haroldadmin.moonshot.notifications.SchedulingWorker.name"
        const val tag: String = "com.haroldadmin.moonshot.notifications.SchedulingWorker.tag"
    }
}