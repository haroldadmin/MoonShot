package com.haroldadmin.moonshot.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager

class TimeChangedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.TIME_SET") {
            val isNotificationsEnabled = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(KEY_LAUNCH_NOTIFICATIONS, true)

            if (isNotificationsEnabled) {
                LaunchNotificationManager(context).scheduleNotifications()
            }
        }
    }
}