package com.haroldadmin.moonshot.notifications

import android.app.Notification
import android.app.NotificationChannel
import androidx.core.app.NotificationManagerCompat

interface SystemNotificationManager {

    fun notify(notificationId: Int, notification: Notification)

    fun createNotificationChannel(channel: NotificationChannel)
}

class RealSystemNotificationManager(
    private val delegate: NotificationManagerCompat
) : SystemNotificationManager {

    override fun notify(notificationId: Int, notification: Notification) {
        delegate.notify(notificationId, notification)
    }

    override fun createNotificationChannel(channel: NotificationChannel) {
        delegate.createNotificationChannel(channel)
    }
}