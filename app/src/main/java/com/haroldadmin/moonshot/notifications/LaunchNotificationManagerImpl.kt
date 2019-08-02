package com.haroldadmin.moonshot.notifications

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.haroldadmin.moonshot.notifications.workers.WeeklyNotificationSchedulingWorker
import com.haroldadmin.moonshot.base.broadcastPendingIntent
import com.haroldadmin.moonshot.base.cancelAlarmWithIntent
import com.haroldadmin.moonshot.base.exactAlarmAt
import com.haroldadmin.moonshot.base.intent
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
import com.haroldadmin.moonshot.notifications.workers.DailyNotificationSchedulingWorker
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
            log("Notifications disabled in settings, not enqueueing notification work")
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

        val dailyWorkRequest =
            PeriodicWorkRequestBuilder<DailyNotificationSchedulingWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

        val weeklyWorkRequest =
            PeriodicWorkRequestBuilder<WeeklyNotificationSchedulingWorker>(7, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

        with(WorkManager.getInstance(context)) {
            enqueueUniquePeriodicWork(
                DailyNotificationSchedulingWorker.NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                dailyWorkRequest
            )
            enqueueUniquePeriodicWork(
                WeeklyNotificationSchedulingWorker.NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                weeklyWorkRequest
            )
        }
    }

    override fun schedule(type: LaunchNotification, time: Long) {
        when (type) {
            JustBeforeLaunch -> scheduleJustBeforeLaunchNotification(time)
            DayBeforeLaunch -> scheduleDayBeforeLaunchNotification(time)
            WeekBeforeLaunch -> scheduleWeekBeforeLaunchNotifications(time)
        }
    }

    override fun disable(type: LaunchNotification) {

        /*
        Any previously scheduled alarms will still fire, but their notifications will not be delivered
        because LaunchAlarmReceiver checks if notifications are enabled before firing any
        */
        when (type) {
            JustBeforeLaunch -> {
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
            DayBeforeLaunch -> {
                log("Cancelling alarm for day-before-launch notification")
                context.cancelAlarmWithIntent {
                    broadcastPendingIntent(DAY_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE) {
                        intent<DayBeforeLaunchAlarmReceiver>()
                    }
                }
                log("Removing existing notifications")
                NotificationManagerCompat
                    .from(context)
                    .cancel(DAY_BEFORE_LAUNCH_NOTIFICATION_ID)
            }
            WeekBeforeLaunch -> {
                log("Cancelling alarm for week-before-launch notification")
                context.cancelAlarmWithIntent {
                    broadcastPendingIntent(WEEK_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE) {
                        intent<WeekBeforeLaunchAlarmReceiver>()
                    }
                }
                log("Removing existing notifications")
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

        context.exactAlarmAt(notificationTime.toDate().time) {
            broadcastPendingIntent(JUST_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE) {
                intent<JustBeforeLaunchAlarmReceiver>()
            }
        }
    }

    private fun scheduleDayBeforeLaunchNotification(time: Long) {
        val notificationTime = LocalDate(time).minusDays(1).toDateTimeAtStartOfDay()

        context.exactAlarmAt(notificationTime.millis) {
            broadcastPendingIntent(DAY_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE) {
                intent<DayBeforeLaunchAlarmReceiver>()
            }
        }
    }

    private fun scheduleWeekBeforeLaunchNotifications(time: Long) {
        val notificationTime = LocalDate(time).toDateTimeAtStartOfDay()
        context.exactAlarmAt(notificationTime.millis) {
            broadcastPendingIntent(WEEK_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE) {
                intent<WeekBeforeLaunchAlarmReceiver>()
            }
        }
    }
}