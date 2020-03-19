package com.haroldadmin.moonshot.notifications.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.haroldadmin.moonshot.di.appComponent
import com.haroldadmin.moonshot.notifications.MoonShotNotificationManager
import com.haroldadmin.moonshot.notifications.NotificationConstants
import com.haroldadmin.moonshot.utils.log
import javax.inject.Inject
import javax.inject.Named

class BootCompleteReceiver : BroadcastReceiver() {

    @Inject
    @Named("settings")
    lateinit var settings: SharedPreferences

    @Inject
    lateinit var manager: MoonShotNotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        log("BOOT_COMPLETED Intent received")
        appComponent().broadcastReceiversComponent().create().inject(this)
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            if (shouldEnableNotifications(settings)) {
                log("Enabling notifications")
                manager.enableNotifications()
            }
        }
    }
}

@Suppress("unused")
fun BroadcastReceiver.shouldEnableNotifications(settings: SharedPreferences): Boolean {
    return settings.getBoolean(NotificationConstants.JustBeforeLaunch.settingsKey, true) ||
            settings.getBoolean(NotificationConstants.DayBeforeLaunch.settingsKey, true)
}