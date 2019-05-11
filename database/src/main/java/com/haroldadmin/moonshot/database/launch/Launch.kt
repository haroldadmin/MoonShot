package com.haroldadmin.moonshot.database.launch

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "launches")
data class Launch(
    @PrimaryKey
    @ColumnInfo(name = "flight_number") val flightNumber: Int,
    @ColumnInfo(name = "mission_name") val missionName: String,
    @ColumnInfo(name = "mission_id") val missionId: List<String>,
    @ColumnInfo(name = "launch_year") val launchYear: String,
    @ColumnInfo(name = "launch_date_utc") val launchDate: Date,
    @ColumnInfo(name = "is_tentative") val isTentative: Boolean,
    @ColumnInfo(name = "tentative_max_precision") val tentativeMaxPrecision: String,
    @ColumnInfo(name = "tbd") val tbd: Boolean,
    @ColumnInfo(name = "launch_window") val launchWindow: Int,
    @ColumnInfo(name = "ships") val ships: List<String>,
    @ColumnInfo(name = "launch_success") val launchSuccess: Boolean,
    @ColumnInfo(name = "details") val details: String,
    @ColumnInfo(name = "upcoming") val upcoming: Boolean,
    @ColumnInfo(name = "static_fire_date_utc") val staticFireDate: Date,
    @Embedded val rocket: RocketSummary,
    @Embedded val telemetry: Telemetry,
    @Embedded val launchSite: LaunchSite,
    @Embedded val links: Links,
    @Embedded val timeline: Timeline
)

