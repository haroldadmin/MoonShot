package com.haroldadmin.spacex_api_wrapper.launches

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date


@JsonClass(generateAdapter = true)
data class Launch (
    @Json(name = "flight_number") val flightNumber: Int,
    @Json(name = "mission_name") val missionName: String,
    @Json(name = "mission_id") val missionId: List<String>,
    @Json(name = "launch_year") val launchYear: String,
    @Json(name = "launch_date_utc") val launchDate: Date,
    @Json(name = "is_tentative") val isTentative: Boolean,
    @Json(name = "tentative_max_precision") val tentativeMaxPrecision: String,
    @Json(name = "tbd") val tbd: Boolean,
    @Json(name = "launch_window") val launchWindow: Int,
    @Json(name = "rocket") val rocket: RocketSummary,
    @Json(name = "ships") val ships: List<String>,
    @Json(name = "telemetry") val telemetry: Telemetry,
    @Json(name = "launch_site") val launchSite: LaunchSite,
    @Json(name = "launch_success") val launchSuccess: Boolean,
    @Json(name = "links") val links: Links,
    @Json(name = "details") val details: String,
    @Json(name = "upcoming") val upcoming: Boolean,
    @Json(name = "static_fire_date_utc") val staticFireDate: Date,
    @Json(name = "timeline") val timeline: Timeline
)

