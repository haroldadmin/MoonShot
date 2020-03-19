package com.haroldadmin.moonshot.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

enum class NotificationType {
    JustBeforeLaunch,
    DayBeforeLaunch,
    ScheduleChange,
    Unknown
}

@Entity(tableName = "notification_records")
data class NotificationRecord(
    @ColumnInfo(name = "launch_flight_number")
    val launchFlightNumber: Int,
    @ColumnInfo(name = "notification_date")
    val notificationDate: Date,
    @ColumnInfo(name = "notification_type")
    val notificationType: NotificationType
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}