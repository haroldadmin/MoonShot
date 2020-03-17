package com.haroldadmin.moonshot.notifications.new

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Named

class DayBeforeLaunchAlarmReceiver : BroadcastReceiver() {

    @Inject
    @Named("settings")
    lateinit var settings: SharedPreferences

    @Inject
    lateinit var notifier: Notifier

    override fun onReceive(context: Context?, intent: Intent?) {

    }
}