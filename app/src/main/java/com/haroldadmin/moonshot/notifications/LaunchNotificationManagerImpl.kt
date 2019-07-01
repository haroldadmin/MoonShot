package com.haroldadmin.moonshot.notifications

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.haroldadmin.moonshot.base.broadcastPendingIntent
import com.haroldadmin.moonshot.base.cancelAlarmWithIntent
import com.haroldadmin.moonshot.base.exactAlarmAt
import com.haroldadmin.moonshot.base.intent
import com.haroldadmin.moonshot.models.LONG_DATE_FORMAT
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager.Companion.DAY_BEFORE_LAUNCH_NOTIFICATION_ID
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager.Companion.DAY_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager.Companion.JUST_BEFORE_LAUNCH_NOTIFICATION_ID
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager.Companion.JUST_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager.Companion.KEY_DAY_BEFORE_LAUNCH_NOTIFICATIONS_SETTING
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager.Companion.KEY_JUST_BEFORE_LAUNCH_NOTIFICATIONS_PADDING_SETTING
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager.Companion.KEY_JUST_BEFORE_LAUNCH_NOTIFICATIONS_SETTING
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager.Companion.KEY_WEEK_BEFORE_LAUNCH_NOTIFICATIONS_SETTING
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager.Companion.WEEK_BEFORE_LAUNCH_NOTIFICATION_ID
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager.Companion.WEEK_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE
import com.haroldadmin.moonshot.notifications.receivers.DayBeforeLaunchAlarmReceiver
import com.haroldadmin.moonshot.notifications.receivers.JustBeforeLaunchAlarmReceiver
import com.haroldadmin.moonshot.notifications.receivers.WeekBeforeLaunchAlarmReceiver
import com.haroldadmin.moonshot.utils.log
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import java.util.concurrent.TimeUnit

class LaunchNotificationManagerImpl(private val context: Context) : LaunchNotificationsManager {

    private val settings = PreferenceManager.getDefaultSharedPreferences(context)

    override fun enable() {
        if (settings.getBoolean(KEY_JUST_BEFORE_LAUNCH_NOTIFICATIONS_SETTING, true) ||
            settings.getBoolean(KEY_DAY_BEFORE_LAUNCH_NOTIFICATIONS_SETTING, true) ||
            settings.getBoolean(KEY_WEEK_BEFORE_LAUNCH_NOTIFICATIONS_SETTING, true)
        ) {

            log("Enabling notifications")
            enqueueNotificationWork()
        } else {
            log("Notifications disabled in settings, not enqueing notification work")
        }
    }

    private fun enqueueNotificationWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresDeviceIdle(false)
            .setRequiresBatteryNotLow(false)
            .setRequiresStorageNotLow(false)
            .setRequiresCharging(false)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<NotificationSchedulingWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(
                NotificationSchedulingWorker.NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
    }

    override fun schedule(type: LaunchNotification, time: Long) {
        when (type) {
            LaunchNotification.JUST_BEFORE -> scheduleJustBeforeLaunchNotification(time)
            LaunchNotification.DAY_BEFORE -> scheduleDayBeforeLaunchNotification(time)
            LaunchNotification.WEEK_BEFORE -> scheduleWeekBeforeLaunchNotifications()
        }
    }

    override fun disable(type: LaunchNotification) {

        /*
        Any previously scheduled alarms will still fire, but their notifications will not be delivered
        because LaunchAlarmReceiver checks if notifications are enabled before firing any
        */
        when (type) {
            LaunchNotification.JUST_BEFORE -> {
                log("Cancelling alarm for just-before-launch notification")
                context.cancelAlarmWithIntent {
                    broadcastPendingIntent(JUST_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE) {
                        intent<JustBeforeLaunchAlarmReceiver>()
                    }
                }
                log("Removing existing notifications")
                NotificationManagerCompat
                    .from(context)
                    .cancel(JUST_BEFORE_LAUNCH_NOTIFICATION_ID)
            }
            LaunchNotification.DAY_BEFORE -> {
                log("Cancelling alarm for day-before-launch notification")
                context.cancelAlarmWithIntent {
                    broadcastPendingIntent(DAY_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE) {
                        intent<DayBeforeLaunchAlarmReceiver>()
                    }
                }
                NotificationManagerCompat
                    .from(context)
                    .cancel(DAY_BEFORE_LAUNCH_NOTIFICATION_ID)
            }
            LaunchNotification.WEEK_BEFORE -> {
                log("Cancelling alarm for week-before-launch notification")
                context.cancelAlarmWithIntent {
                    broadcastPendingIntent(WEEK_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE) {
                        intent<WeekBeforeLaunchAlarmReceiver>()
                    }
                }
                NotificationManagerCompat
                    .from(context)
                    .cancel(WEEK_BEFORE_LAUNCH_NOTIFICATION_ID)
            }
        }
    }

    private fun scheduleJustBeforeLaunchNotification(time: Long) {

        val paddingMillis =
            settings.getInt(KEY_JUST_BEFORE_LAUNCH_NOTIFICATIONS_PADDING_SETTING, 30) * 60 * 1000

        val notificationTime = LocalDateTime(time).minusMillis(paddingMillis).toDateTime()

        log("Scheduling JBL notification at ${notificationTime.toString(LONG_DATE_FORMAT)}")

        context.exactAlarmAt(notificationTime.toDate().time) {
            broadcastPendingIntent(JUST_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE) {
                intent<JustBeforeLaunchAlarmReceiver>()
            }
        }
    }

    private fun scheduleDayBeforeLaunchNotification(time: Long) {
        val notificationTime = LocalDate(time).minusDays(1).toDateTimeAtStartOfDay()

        log("Scheduling DBL notification at ${notificationTime.toString(LONG_DATE_FORMAT)}")

        context.exactAlarmAt(notificationTime.millis) {
            broadcastPendingIntent(DAY_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE) {
                intent<DayBeforeLaunchAlarmReceiver>()
            }
        }
    }

    private fun scheduleWeekBeforeLaunchNotifications() {
        val notificationTime = LocalDate.now().toDateTimeAtStartOfDay()

        log("Scheduling WBL notifications at ${notificationTime.toString(LONG_DATE_FORMAT)}")

        context.exactAlarmAt(notificationTime.millis) {
            broadcastPendingIntent(WEEK_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE) {
                intent<WeekBeforeLaunchAlarmReceiver>()
            }
        }
    }
}