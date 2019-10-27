package com.haroldadmin.moonshot.notifications

import android.app.Notification

interface LaunchNotificationsManager {

    companion object {
        // Notification channel constants
        const val JUST_BEFORE_LAUNCH_CHANNEL_ID = "channel-primary"
        const val DAY_BEFORE_LAUNCH_CHANNEL_ID = "day-before-launch-channel"

        // Notification Manager constants
        const val JUST_BEFORE_LAUNCH_NOTIFICATION_ID = 0
        const val DAY_BEFORE_LAUNCH_NOTIFICATION_ID = 1

        // Settings constants
        const val KEY_JUST_BEFORE_LAUNCH_NOTIFICATIONS_SETTING = "launch-notifications"
        const val KEY_JUST_BEFORE_LAUNCH_NOTIFICATIONS_PADDING_SETTING = "notification-padding"
        const val JUST_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE = 0

        const val KEY_DAY_BEFORE_LAUNCH_NOTIFICATIONS_SETTING = "day-before-launch-notifications"
        const val DAY_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE = 1
    }

    fun enable()

    fun schedule(type: LaunchNotification, time: Long)

    fun disable(type: LaunchNotification)

    fun notify(type: LaunchNotification, notification: Notification)
}
