package com.haroldadmin.moonshot.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BootCompleteReceiver : BroadcastReceiver(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val isNotificationsEnabled = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(LaunchNotificationManager.KEY_LAUNCH_NOTIFICATIONS, true)

            if (isNotificationsEnabled)
                launch { LaunchNotificationManager(context).scheduleNotifications() }
        }
    }
}