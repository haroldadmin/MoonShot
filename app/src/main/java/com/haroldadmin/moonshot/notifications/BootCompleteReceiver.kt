package com.haroldadmin.moonshot.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.preference.PreferenceManager

class BootCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val isNotificationsEnabled = PreferenceManager
            .getDefaultSharedPreferences(context)
            .getBoolean(KEY_LAUNCH_NOTIFICATIONS, true)

        if (isNotificationsEnabled)
            LaunchNotificationManager(context).scheduleNotifications()
    }
}