package com.haroldadmin.moonshot

import android.app.Notification
import android.app.NotificationChannel
import com.haroldadmin.moonshot.notifications.SystemNotificationManager
import javax.inject.Inject

class FakeNotificationManager @Inject constructor() : SystemNotificationManager {

    val channels = mutableListOf<NotificationChannel>()
    val notifications = mutableListOf<Pair<Int, Notification>>()

    override fun notify(notificationId: Int, notification: Notification) {
        notifications.add(notificationId to notification)
    }

    override fun createNotificationChannel(channel: NotificationChannel) {
        channels.add(channel)
    }

    fun clear() {
        channels.clear()
        notifications.clear()
    }
}