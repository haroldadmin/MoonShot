package com.haroldadmin.moonshot.notifications.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager
import com.haroldadmin.moonshot.notifications.DayBeforeLaunch
import com.haroldadmin.moonshot.notifications.JustBeforeLaunch
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import org.koin.core.KoinComponent
import org.koin.core.get

class BootCompleteReceiver : BroadcastReceiver(), KoinComponent {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            if (shouldEnableNotifications(context)) {
                get<LaunchNotificationsManager>().enable()
            }
        }
    }
}

@Suppress("unused")
fun BroadcastReceiver.shouldEnableNotifications(context: Context): Boolean {
    val settings = PreferenceManager.getDefaultSharedPreferences(context)
    return settings.getBoolean(JustBeforeLaunch.enabledPreferenceKey, true) ||
            settings.getBoolean(DayBeforeLaunch.enabledPreferenceKey, true)
}