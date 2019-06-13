package com.haroldadmin.moonshot.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import com.haroldadmin.moonshot.utils.log

class LaunchNotificationManager(val context: Context) {

    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val settings: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val preferences: SharedPreferences = context.getSharedPreferences(MOONSHOT_SHARED_PREFS, Context.MODE_PRIVATE)

    fun scheduleNotifications(launchPadding: Int? = null) {
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

    fun disableNotifications() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, LaunchAlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        log("Cancelling launch notification alarm")
        alarmManager.cancel(pendingIntent)

        NotificationManagerCompat.from(context).cancelAll()
    }
}