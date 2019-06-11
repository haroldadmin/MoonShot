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
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun scheduleNotifications() {
        val padding = preferences.getInt(KEY_NOTIFICATION_PADDING, 10)
        val launchTime = preferences.getLong(KEY_LAUNCH_NAME, Long.MAX_VALUE)

        val notifyIntent = Intent(context, LaunchAlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.RTC_WAKEUP,
            launchTime - padding * 60 * 1000,
            pendingIntent
        )
    }

    fun disableNotifications() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, LaunchAlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        alarmManager.cancel(pendingIntent)

        NotificationManagerCompat.from(context).cancelAll()
    }
}