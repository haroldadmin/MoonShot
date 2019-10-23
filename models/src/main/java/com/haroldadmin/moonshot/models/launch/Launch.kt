package com.haroldadmin.moonshot.models.launch

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.models.DatePrecision
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "launches")
data class Launch(
    @PrimaryKey
    @ColumnInfo(name = "flight_number") val flightNumber: Int,
    @ColumnInfo(name = "mission_name") val missionName: String,
    @ColumnInfo(name = "mission_id") val missionId: List<String>,
    @ColumnInfo(name = "launch_year") val launchYear: String,
    @ColumnInfo(name = "launch_date_utc") val launchDate: Date,
    @ColumnInfo(name = "is_tentative") val isTentative: Boolean,
    @ColumnInfo(name = "tentative_max_precision") val tentativeMaxPrecision: DatePrecision,
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
            DatePrecision.hour,
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
    val missionPatch = links?.missionPatchSmall ?: links?.missionPatch
}

data class LaunchMinimal(
    @ColumnInfo(name = "flight_number")
    val flightNumber: Int,
    @ColumnInfo(name = "mission_name")
    val missionName: String,
    @ColumnInfo(name = "missionPatchSmall")
    val missionPatch: String?,
    @ColumnInfo(name = "launch_date_utc")
    val launchDate: Date,
    @ColumnInfo(name = "launch_success")
    val launchSuccess: Boolean?,
    @ColumnInfo(name = "details")
    val details: String?,
    @ColumnInfo(name = "siteName")
    val siteName: String?,
    @ColumnInfo(name = "siteNameLong")
    val siteNameLong: String?,
    @ColumnInfo(name = "siteId")
    val siteId: String?,
    @ColumnInfo(name = "youtubeKey")
    val youtubeKey: String?,
    @ColumnInfo(name = "redditCampaign")
    val redditCampaign: String?,
    @ColumnInfo(name = "redditLaunch")
    val redditLaunch: String?,
    @ColumnInfo(name = "redditMedia")
    val redditMedia: String?,
    @ColumnInfo(name = "wikipedia")
    val wikipedia: String?,
    @ColumnInfo(name = "tbd")
    val tbd: Boolean,
    @ColumnInfo(name = "is_tentative")
    val isTentative: Boolean,
    @ColumnInfo(name = "tentative_max_precision")
    val maxPrecision: DatePrecision
) {

    @Ignore
    val launchDateText: String = SimpleDateFormat(maxPrecision.dateFormat, Locale.US).format(launchDate)

    @Ignore
    val links = mapOf(
        "YouTube" to youtubeKey,
        "Reddit Campaign" to redditCampaign,
        "Reddit Launch" to redditLaunch,
        "Reddit Media" to redditMedia,
        "Wikipedia" to wikipedia
    )
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
    val rocketType: String,
    @ColumnInfo(name = "id")
    val rocketId: String
)

data class LaunchPictures(
    @ColumnInfo(name = "flickrImages")
    val images: List<String>
)

fun Launch.toLaunchMinimal(): LaunchMinimal {
    val dbLaunch = this
    return LaunchMinimal(
        flightNumber = dbLaunch.flightNumber,
        missionName = dbLaunch.missionName,
        missionPatch = dbLaunch.missionPatch,
        launchDate = dbLaunch.launchDate,
        launchSuccess = dbLaunch.launchSuccess,
        details = dbLaunch.details,
        siteName = dbLaunch.launchSite?.siteName,
        siteNameLong = dbLaunch.launchSite?.siteNameLong,
        siteId = dbLaunch.launchSite?.siteId,
        youtubeKey = dbLaunch.links?.youtubeKey,
        redditCampaign = dbLaunch.links?.redditCampaign,
        redditLaunch = dbLaunch.links?.redditLaunch,
        redditMedia = dbLaunch.links?.redditMedia,
        wikipedia = dbLaunch.links?.wikipedia,
        tbd = dbLaunch.tbd,
        isTentative = dbLaunch.isTentative,
        maxPrecision = dbLaunch.tentativeMaxPrecision
    )
}