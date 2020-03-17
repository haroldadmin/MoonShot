package com.haroldadmin.moonshot.notifications.new

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.haroldadmin.moonshot.models.NotificationType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class JustBeforeLaunchAlarmReceiver : BroadcastReceiver() {

    @Inject lateinit var notifier: Notifier

    @ExperimentalCoroutinesApi
    override fun onReceive(context: Context?, intent: Intent?) {
        notifier.processBroadcast(NotificationType.JustBeforeLaunch)
    }

}