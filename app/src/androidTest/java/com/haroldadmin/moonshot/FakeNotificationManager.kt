package com.haroldadmin.moonshot

import android.app.NotificationChannel
import com.haroldadmin.moonshot.notifications.SystemNotificationManager

class FakeNotificationManager : SystemNotificationManager {

    private val channels = mutableListOf<NotificationChannel>()
    private val notifications = mutableListOf<Pair<Int, NotificationChannel>>()

}