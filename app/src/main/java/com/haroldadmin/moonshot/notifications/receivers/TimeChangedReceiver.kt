package com.haroldadmin.moonshot.notifications.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.haroldadmin.moonshot.di.appComponent
import com.haroldadmin.moonshot.notifications.MoonShotNotificationManager
import com.haroldadmin.moonshot.utils.log
import javax.inject.Inject
import javax.inject.Named

class TimeChangedReceiver : BroadcastReceiver() {

    @Inject
    @Named("settings")
    lateinit var settings: SharedPreferences

    @Inject
    lateinit var manager: MoonShotNotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        log("TIME_SET Intent received")
        appComponent().broadcastReceiversComponent().create().inject(this)
        if (intent.action == "android.intent.action.TIME_SET") {
            if (shouldEnableNotifications(settings)) {
                log("Enabling notifications")
                manager.enableNotifications()
            }
        }
    }
}