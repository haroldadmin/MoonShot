package com.haroldadmin.moonshot.notifications.new

import android.content.Context
import android.content.SharedPreferences
import com.haroldadmin.logger.logW
import com.haroldadmin.moonshot.base.broadcastPendingIntent
import com.haroldadmin.moonshot.base.exactAlarmAt
import com.haroldadmin.moonshot.base.intent
import com.haroldadmin.moonshot.models.NotificationType
import javax.inject.Inject
import javax.inject.Named

/**
 * Responsible for scheduling system alarms at the given time.
 *
 * AlarmScheduler is aware of the different types of notification that can be posted by the app,
 * and takes into account notification padding time when scheduling alarms.
 */
class AlarmScheduler @Inject constructor(
    private val context: Context,
    @Named("settings")
    private val settings: SharedPreferences
) {

    fun schedule(notificationType: NotificationType, timeUTC: Long) {
        when (notificationType) {
            NotificationType.JustBeforeLaunch -> scheduleJustBeforeLaunchNotification(timeUTC)
            NotificationType.DayBeforeLaunch -> scheduleDayBeforeLaunchNotification(timeUTC)
            else -> logW { "Unknown notification type provided for scheduling: $notificationType" }
        }
    }

    private fun scheduleJustBeforeLaunchNotification(timeUTC: Long) {
        val paddingMins = settings.getInt(NotificationConstants.JustBeforeLaunch.paddingKey, 30)
        val notificationTime = timeUTC - paddingMins * 60
        context.exactAlarmAt(notificationTime) {
            broadcastPendingIntent(NotificationConstants.JustBeforeLaunch.notificationRequestCode) {
                intent<JustBeforeLaunchAlarmReceiver>()
            }
        }
    }

    private fun scheduleDayBeforeLaunchNotification(timeUTC: Long) {
        context.exactAlarmAt(timeUTC) {
            broadcastPendingIntent(NotificationConstants.DayBeforeLaunch.notificationRequestCode) {
                intent<DayBeforeLaunchAlarmReceiver>()
            }
        }
    }
}