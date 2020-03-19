package com.haroldadmin.moonshot.notifications

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.haroldadmin.moonshot.base.broadcastPendingIntent
import com.haroldadmin.moonshot.base.cancelAlarmWithIntent
import com.haroldadmin.moonshot.base.intent
import com.haroldadmin.moonshot.core.unsyncedLazy
import com.haroldadmin.moonshot.models.NotificationType
import com.haroldadmin.moonshot.notifications.receivers.DayBeforeLaunchAlarmReceiver
import com.haroldadmin.moonshot.notifications.receivers.JustBeforeLaunchAlarmReceiver
import com.haroldadmin.moonshot.notifications.NotificationConstants.DayBeforeLaunch
import com.haroldadmin.moonshot.notifications.NotificationConstants.JustBeforeLaunch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MoonShotNotificationManager @Inject constructor(
    private val context: Context,
    private val workManager: WorkManager
) {

    /**
     * Creates and enqueues a work Request for the [AlarmSchedulingWorker].
     * The SchedulingWorker will run every [NotificationConstants.SchedulingWorker.repeatIntervalHours], and will
     * schedule notification alarms on each run if it completes successfully. If unsuccessful, it will repeat according
     * to the default backoff policy of WorkManager.
     *
     * This method does not care which [NotificationType] is enabled in settings. Repeated invocations of this
     * method have no effect, since the existing enqueued work is treated with [ExistingPeriodicWorkPolicy.KEEP].
     * Therefore it is safe to call this method whenever the notifications for any [NotificationType] are enabled.
     */
    fun enableNotifications() {
        val workRequest = PeriodicWorkRequestBuilder<AlarmSchedulingWorker>(
            repeatInterval = NotificationConstants.SchedulingWorker.repeatIntervalHours,
            repeatIntervalTimeUnit = TimeUnit.HOURS,
            flexTimeInterval = NotificationConstants.SchedulingWorker.flexIntervalMinutes,
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
     * Cancels enqueued work for scheduling notifications, scheduled alarms and also removes
     * existing notifications, if any.
     *
     * This method should only be called if all [NotificationType] have been disabled in the settings.
     * If any subset of [NotificationType] is still enabled, this method should NOT be called.
     */
    fun disableAllNotifications() {
        cancelSchedulingWork()
        cancelScheduledAlarms()
        removeExistingNotifications()
    }

    private val workReqConstraints by unsyncedLazy {
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(false)
            .setRequiresCharging(false)
            .setRequiresDeviceIdle(false)
            .setRequiresStorageNotLow(false)
            .build()
    }

    private fun cancelSchedulingWork() {
        workManager.cancelAllWorkByTag(AlarmSchedulingWorker.tag)
    }

    private fun cancelScheduledAlarms() {
        context.cancelAlarmWithIntent {
            broadcastPendingIntent(JustBeforeLaunch.notificationRequestCode) {
                context.intent<JustBeforeLaunchAlarmReceiver>()
            }
        }
        context.cancelAlarmWithIntent {
            broadcastPendingIntent(DayBeforeLaunch.notificationRequestCode) {
                context.intent<DayBeforeLaunchAlarmReceiver>()
            }
        }
    }

    private fun removeExistingNotifications() {
        NotificationManagerCompat.from(context).apply {
            cancel(JustBeforeLaunch.notificationId)
            cancel(DayBeforeLaunch.notificationId)
        }
    }
}
