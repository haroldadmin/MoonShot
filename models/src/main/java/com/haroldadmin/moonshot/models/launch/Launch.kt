package com.haroldadmin.moonshot.models.launch

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
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
    @ColumnInfo(name = "launch_window") val launchWindow: Int?,
    @ColumnInfo(name = "ships") val ships: List<String>,
    @ColumnInfo(name = "launch_success") val launchSuccess: Boolean?,
    @ColumnInfo(name = "details") val details: String?,
    @ColumnInfo(name = "upcoming") val upcoming: Boolean?,
    @ColumnInfo(name = "static_fire_date_utc") val staticFireDate: Date?,
    @Embedded val telemetry: Telemetry?,
    @Embedded val launchSite: LaunchSite?,
    @Embedded val links: Links?,
    @Embedded val timeline: Timeline?
) {
    companion object {
        fun getSampleLaunch() = Launch(
            65,
            "Telstar 19V",
            listOf("F4F83DE"),
            "2018",
            Date(),
            false,
            "hour",
            false,
            7200,
            listOf(),
            true,
            "",
            false,
            Date(),
            Telemetry.getSampleTelemetry(),
            LaunchSite.getSampleLaunchSite(),
            Links.getSampleLinks(),
            Timeline.getSampleTimeline()
        )
    }

    @Ignore
    val backdropImageUrl = links?.flickrImages?.firstOrNull() ?: links?.redditMedia
    @Ignore
    val missionPatch = links?.missionPatchSmall ?: links?.missionPatch
}

data class LaunchMinimal(
    @ColumnInfo(name = "flight_number")
    val flightNumber: Int,
    @ColumnInfo(name = "mission_name")
    val missionName: String?,
    @ColumnInfo(name = "missionPatchSmall")
    val missionPatch: String?,
    @ColumnInfo(name = "launch_date_utc")
    val launchDate: Date?,
    @ColumnInfo(name = "details")
    val details: String?,
    @ColumnInfo(name = "siteName")
    val siteName: String?,
    @ColumnInfo(name = "siteNameLong")
    val siteNameLong: String?,
    @ColumnInfo(name = "siteId")
    val siteId: String?
) {
    @Ignore
    val launchYear: String = SimpleDateFormat("YYYY").format(launchDate)
}

data class LaunchStats(
    @Embedded(prefix = "rocket_")
    val rocket: RocketSummaryMinimal?,
    @ColumnInfo(name = "core_count")
    val firstStageCoreCounts: Int,
    @ColumnInfo(name = "payload_count")
    val secondStagePayloadCounts: Int
)

data class RocketSummaryMinimal(
    @ColumnInfo(name = "name")
    val rocketName: String,
    @ColumnInfo(name = "type")
    val rocketType: String
)

data class LaunchPictures(
    @ColumnInfo(name = "flickrImages")
    val images: List<String>
)