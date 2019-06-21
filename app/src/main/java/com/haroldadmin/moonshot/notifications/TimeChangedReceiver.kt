package com.haroldadmin.moonshot.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TimeChangedReceiver : BroadcastReceiver(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.TIME_SET") {
            val isNotificationsEnabled = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(KEY_LAUNCH_NOTIFICATIONS, true)

            if (isNotificationsEnabled) {
                launch { LaunchNotificationManager(context).scheduleNotifications() }
            }
        }
    }
}