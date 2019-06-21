package com.haroldadmin.moonshot.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import com.haroldadmin.moonshot.MoonShot
import com.haroldadmin.moonshot.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LaunchNotificationManager(val context: Context) {

    companion object {
        // Notification channel constants
        const val PRIMARY_CHANNEL_ID = "Channel to deliver launch notifications"

        // Notification Manager constants
        const val NOTIFICATION_ID = 0

        // Settings constants
        const val KEY_LAUNCH_NOTIFICATIONS = "launch-notifications"
        const val KEY_NOTIFICATION_PADDING = "notification-padding"

        // Shared Preference keys
        const val KEY_LAUNCH_NAME = "launch-name"
        const val KEY_LAUNCH_SITE = "launch-site"
        const val KEY_LAUNCH_DATE = "launch-date"
        const val KEY_LAUNCH_TIME = "launch-time"
        const val KEY_LAUNCH_MISSION_PATCH = "launch-mission-patch"
        const val KEY_FLIGHT_NUMBER = "launch-flight-number"
    }

    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val settings: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val preferences: SharedPreferences = context.getSharedPreferences(MoonShot.MOONSHOT_SHARED_PREFS, Context.MODE_PRIVATE)

    suspend fun scheduleNotifications(launchPadding: Int? = null) = withContext(Dispatchers.Default) {
        val padding: Int = launchPadding ?: settings.getInt(KEY_NOTIFICATION_PADDING, 30) ?: 30
        val launchTime = preferences.getLong(KEY_LAUNCH_TIME, Long.MAX_VALUE)

        val notifyIntent = Intent(context, LaunchAlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        log("Setting alarm for launch notification on ${launchTime - (padding * 60 * 1000)}")
        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.RTC_WAKEUP,
            launchTime - (padding * 60 * 1000),
            pendingIntent
        )
    }

    suspend fun disableNotifications() = withContext(Dispatchers.Default) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, LaunchAlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        log("Cancelling launch notification alarm")
        alarmManager.cancel(pendingIntent)

        NotificationManagerCompat.from(context).cancelAll()
    }
}
