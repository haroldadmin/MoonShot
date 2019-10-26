package com.haroldadmin.moonshot.notifications.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import org.koin.core.KoinComponent
import org.koin.core.get

class TimeChangedReceiver : BroadcastReceiver(), KoinComponent {

    override fun onReceive(context: Context, intent: Intent) {
//        if (intent.action == "android.intent.action.TIME_SET") {
//            get<LaunchNotificationsManager>().enable()
//        }
    }
}