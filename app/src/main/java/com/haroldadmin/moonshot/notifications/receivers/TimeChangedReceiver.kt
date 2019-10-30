package com.haroldadmin.moonshot.notifications.receivers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import javax.inject.Inject
import javax.inject.Named

class TimeChangedReceiver : CoroutineBroadcastReceiver() {

    @Inject
    @Named("settings")
    lateinit var settings: SharedPreferences

    @Inject
    lateinit var manager: LaunchNotificationsManager

    override suspend fun onBroadcastReceived(context: Context, intent: Intent) {
        broadcastReceiverComponent.inject(this)
        if (intent.action == "android.intent.action.TIME_SET") {
            if (shouldEnableNotifications(settings)) {
                manager.enable()
            }
        }
    }
}