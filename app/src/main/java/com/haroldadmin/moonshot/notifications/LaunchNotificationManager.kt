package com.haroldadmin.moonshot.notifications

enum class LaunchNotification : MoonShotNotification {
    JUST_BEFORE, DAY_BEFORE, WEEK_BEFORE
}

interface LaunchNotificationsManager {

    companion object {
        // Notification channel constants
        const val JUST_BEFORE_LAUNCH_CHANNEL_ID = "channel-primary"
        const val DAY_BEFORE_LAUNCH_CHANNEL_ID = "day-before-launch-channel"
        const val WEEK_BEFORE_LAUNCH_CHANNEL_ID = "week-before-launch-channel"
        const val WEEK_BEFORE_LAUNCH_GROUP_ID = "week-before-launch-group"

        // Notification Manager constants
        const val JUST_BEFORE_LAUNCH_NOTIFICATION_ID = 0
        const val DAY_BEFORE_LAUNCH_NOTIFICATION_ID = 1
        const val WEEK_BEFORE_LAUNCH_NOTIFICATION_ID = 2

        // Settings constants
        const val KEY_JUST_BEFORE_LAUNCH_NOTIFICATIONS_SETTING = "launch-notifications"
        const val KEY_JUST_BEFORE_LAUNCH_NOTIFICATIONS_PADDING_SETTING = "notification-padding"
        const val JUST_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE = 0

        const val KEY_DAY_BEFORE_LAUNCH_NOTIFICATIONS_SETTING = "day-before-launch-notifications"
        const val DAY_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE = 1

        const val KEY_WEEK_BEFORE_LAUNCH_NOTIFICATIONS_SETTING = "week-before-launch-notifications"
        const val WEEK_BEFORE_LAUNCH_NOTIFICATION_REQUEST_CODE = 2
    }

    fun enable()

    fun schedule(type: LaunchNotification, time: Long)

    fun disable(type: LaunchNotification)
}
