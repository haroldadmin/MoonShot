package com.haroldadmin.moonshot.notifications

import android.app.Notification
import android.content.Context

interface MoonShotNotification {

    fun create(context: Context, content: LaunchNotificationContent): Notification

    fun createChannel(context: Context)
}
