package com.haroldadmin.moonshot.notifications.new

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.haroldadmin.moonshot.core.unsyncedLazy
import com.haroldadmin.moonshot.models.NotificationType
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MoonShotNotificationManager @Inject constructor(
    private val workManager: WorkManager
) {

    private val workReqConstraints by unsyncedLazy {
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(false)
            .setRequiresCharging(false)
            .setRequiresDeviceIdle(false)
            .setRequiresStorageNotLow(false)
            .build()
    }

    /**
     * Creates and enqueues a work Request for the [AlarmSchedulingWorker].
     * The SchedulingWorker will run every [NotificationConstants.repeatIntervalHours], and will schedule notification alarms on
     * each run if it completes successfully. If unsuccessful, it will repeat according to the default
     * backoff policy of WorkManager.
     *
     * This method does not care which [NotificationType] is enabled in settings. Repeated invocations of this
     * method have no effect, since the existing enqueued work is treated with [ExistingPeriodicWorkPolicy.KEEP].
     * Therefore it is safe to call this method whenever the notifications for any [NotificationType] are enabled.
     */
    fun enableNotifications() {
        val workRequest = PeriodicWorkRequestBuilder<AlarmSchedulingWorker>(
            repeatInterval = NotificationConstants.repeatIntervalHours,
            repeatIntervalTimeUnit = TimeUnit.HOURS,
            flexTimeInterval = NotificationConstants.flexIntervalMinutes,
            flexTimeIntervalUnit = TimeUnit.MINUTES
        )

        workRequest.setConstraints(workReqConstraints)
        workRequest.addTag(AlarmSchedulingWorker.tag)

        workManager.enqueueUniquePeriodicWork(
            AlarmSchedulingWorker.name,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest.build()
        )
    }

    /**
     * Cancels enqueued work for scheduling notifications and scheduled alarms.
     *
     * This method should only be called if all [NotificationType] have been disabled in the settings.
     * If any subset of [NotificationType] is still enabled, this method should NOT be called.
     */
    fun disableAllNotifications() {
        workManager.cancelAllWorkByTag(AlarmSchedulingWorker.tag)
        // TODO "Figure out how to cancel scheduled alarms"
    }

}

object NotificationConstants {

    const val repeatIntervalHours: Long = 6
    const val flexIntervalMinutes: Long = 6 * 60 - 5  // 6 Hours minus 5 minutes

    object JustBeforeLaunch {
        const val settingsKey: String = "launch-notifications"
        const val paddingKey: String = "notification-padding"
        const val channelId = "channel-primary"
        const val notificationRequestCode: Int = 0
        const val notifcationId: Int = 0
    }

    object DayBeforeLaunch {
        const val settingsKey: String = "day-before-launch-notifications"
        const val channelId: String = "day-before-launch-channel"
        const val notificationRequestCode: Int = 1
        const val notificationId = 1
    }
}
