package com.haroldadmin.moonshot.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import androidx.preference.PreferenceManager
import coil.Coil
import coil.api.get
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.base.broadcastPendingIntent
import com.haroldadmin.moonshot.base.cancelAlarmWithIntent
import com.haroldadmin.moonshot.base.exactAlarmAt
import com.haroldadmin.moonshot.base.intent
import com.haroldadmin.moonshot.notifications.receivers.DayBeforeLaunchAlarmReceiver
import com.haroldadmin.moonshot.notifications.receivers.JustBeforeLaunchAlarmReceiver
import com.haroldadmin.moonshot.utils.log
import kotlinx.coroutines.runBlocking
import org.joda.time.LocalDateTime

sealed class LaunchNotification : MoonShotNotification {

    abstract val requestCode: Int
    abstract val notificationId: Int
    abstract val channelId: String
    abstract val enabledPreferenceKey: String

    abstract fun alarmIntent(context: Context): Intent

    fun getMissionPatch(url: String?): Bitmap = runBlocking {
        if (url.isNullOrBlank()) {
            Coil.get(R.drawable.ic_round_rocket_small)
        } else {
            Coil.get(url)
        }.toBitmap()
    }

    fun cancelIntent(context: Context) {
        log("Cancelling intent for ${this::class.java.simpleName}")
        context.cancelAlarmWithIntent {
            broadcastPendingIntent(requestCode) { alarmIntent(context) }
        }
    }

    fun removeNotifications(context: Context) {
        log("Removing notifications for ${this::class.java.simpleName}")
        NotificationManagerCompat.from(context)
            .cancel(notificationId)
    }

    open fun schedule(context: Context, time: Long) {
        cancelIntent(context)
        val notificationTime = LocalDateTime(time).toDateTime()
        context.exactAlarmAt(notificationTime.millis) {
            broadcastPendingIntent(requestCode) {
                alarmIntent(context)
            }
        }
    }
}

object JustBeforeLaunch : LaunchNotification() {

    override val requestCode: Int = LaunchNotificationsManager.JUST_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE
    override val notificationId: Int = LaunchNotificationsManager.JUST_BEFORE_LAUNCH_NOTIFICATION_ID
    override val channelId: String = LaunchNotificationsManager.JUST_BEFORE_LAUNCH_CHANNEL_ID
    override val enabledPreferenceKey: String = LaunchNotificationsManager.KEY_JUST_BEFORE_LAUNCH_NOTIFICATIONS_SETTING
    private const val paddingPreferenceKey: String = LaunchNotificationsManager.KEY_JUST_BEFORE_LAUNCH_NOTIFICATIONS_PADDING_SETTING

    override fun alarmIntent(context: Context): Intent {
        return context.intent<JustBeforeLaunchAlarmReceiver>()
    }

    override fun schedule(context: Context, time: Long) {
        cancelIntent(context)
        val settings = PreferenceManager.getDefaultSharedPreferences(context)
        val paddingMinutes = settings.getInt(paddingPreferenceKey, 30)
        val notificationTime = LocalDateTime(time).minusMinutes(paddingMinutes).toDateTime()

        context.exactAlarmAt(notificationTime.toDate().time) {
            broadcastPendingIntent(LaunchNotificationsManager.JUST_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE) {
                intent<JustBeforeLaunchAlarmReceiver>()
            }
        }
    }

    override fun create(context: Context, content: LaunchNotificationContent): Notification {
        val icon = getMissionPatch(content.missionPatch)

        createChannel(context)

        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_round_rocket_small)
            .setLargeIcon(icon)
            .setContentTitle("${content.name} Launch")
            .setContentText("Launches at ${content.site} on ${content.date}")
            .setContentIntent(getLaunchDetailsPendingIntent(context, content.flightNumber))
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()
    }

    private fun getLaunchDetailsPendingIntent(context: Context, flightNumber: Int): PendingIntent {
        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.launchDetails)
            .setArguments(bundleOf("flightNumber" to flightNumber))
            .createPendingIntent()
    }

    override fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                context.getString(R.string.launchNotificationsJustBeforeLaunchChannelName),
                NotificationManager.IMPORTANCE_HIGH
            )
            NotificationManagerCompat.from(context).createNotificationChannel(channel)
        }
    }
}

object DayBeforeLaunch : LaunchNotification() {

    override val requestCode: Int = LaunchNotificationsManager.DAY_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE
    override val notificationId: Int = LaunchNotificationsManager.DAY_BEFORE_LAUNCH_NOTIFICATION_ID
    override val channelId: String = LaunchNotificationsManager.DAY_BEFORE_LAUNCH_CHANNEL_ID
    override val enabledPreferenceKey: String = LaunchNotificationsManager.KEY_DAY_BEFORE_LAUNCH_NOTIFICATIONS_SETTING

    override fun alarmIntent(context: Context): Intent {
        return context.intent<DayBeforeLaunchAlarmReceiver>()
    }

    override fun create(context: Context, content: LaunchNotificationContent): Notification {
        createChannel(context)

        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_round_rocket_small)
            .setLargeIcon(getMissionPatch(content.missionPatch))
            .setContentTitle("${content.name} Launch")
            .setContentText("Launches tomorrow at ${content.site} on ${content.date}")
            .setContentIntent(getLaunchDetailsPendingIntent(context, content.flightNumber))
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()
    }

    private fun getLaunchDetailsPendingIntent(context: Context, flightNumber: Int): PendingIntent {
        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.launchDetails)
            .setArguments(bundleOf("flightNumber" to flightNumber))
            .createPendingIntent()
    }

    override fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                context.getString(R.string.launchNotificationsDayBeforeLaunchChannelName),
                NotificationManager.IMPORTANCE_DEFAULT
            )

            NotificationManagerCompat.from(context).createNotificationChannel(channel)
        }
    }
}
