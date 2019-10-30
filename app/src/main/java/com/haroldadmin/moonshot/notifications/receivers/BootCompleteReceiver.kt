package com.haroldadmin.moonshot.notifications.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.haroldadmin.moonshot.notifications.DayBeforeLaunch
import com.haroldadmin.moonshot.notifications.JustBeforeLaunch
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import javax.inject.Inject
import javax.inject.Named

class BootCompleteReceiver : CoroutineBroadcastReceiver() {

    @Inject
    @Named("settings")
    lateinit var settings: SharedPreferences

    @Inject
    lateinit var manager: LaunchNotificationsManager

    override suspend fun onBroadcastReceived(context: Context, intent: Intent) {
        broadcastReceiverComponent.inject(this)
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            if (shouldEnableNotifications(settings)) {
                manager.enable()
            }
        }
    }
}

@Suppress("unused")
fun BroadcastReceiver.shouldEnableNotifications(settings: SharedPreferences): Boolean {
    return settings.getBoolean(JustBeforeLaunch().enabledPreferenceKey, true) ||
            settings.getBoolean(DayBeforeLaunch().enabledPreferenceKey, true)
}