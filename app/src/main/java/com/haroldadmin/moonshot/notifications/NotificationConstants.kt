package com.haroldadmin.moonshot.notifications

object NotificationConstants {

    object SchedulingWorker {
        const val repeatIntervalHours: Long = 6
        const val flexIntervalMinutes: Long = 6 * 60 - 5 // 6 Hours minus 5 minutes
    }

    object JustBeforeLaunch {
        const val settingsKey: String = "launch-notifications"
        const val paddingKey: String = "notification-padding"
        const val channelId = "channel-primary"
        const val notificationRequestCode: Int = 0
        const val notificationId: Int = 0
    }

    object DayBeforeLaunch {
        const val settingsKey: String = "day-before-launch-notifications"
        const val channelId: String = "day-before-launch-channel"
        const val notificationRequestCode: Int = 1
        const val notificationId = 1
    }

    object ScheduleChange {
        const val settingsKey: String = "schedule-change-notifications"
        const val channelId: String = "schedule-change-channel"
        const val notificationRequestCode: Int = 2
        const val notificationId: Int = 2
    }
}