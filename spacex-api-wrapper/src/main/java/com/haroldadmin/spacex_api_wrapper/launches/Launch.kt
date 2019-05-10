package com.haroldadmin.spacex_api_wrapper.launches

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date


@JsonClass(generateAdapter = true)
data class Launch (
    @field:Json(name = "flight_number") val flightNumber: Int,
    @field:Json(name = "mission_name") val missionName: String,
    @field:Json(name = "mission_id") val missionId: List<String>,
    @field:Json(name = "launch_year") val launchYear: String,
    @field:Json(name = "launch_date_utc") val launchDate: Date,
    @field:Json(name = "is_tentative") val isTentative: Boolean,
    @field:Json(name = "tentative_max_precision") val tentativeMaxPrecision: String,
    @field:Json(name = "tbd") val tbd: Boolean,
    @field:Json(name = "launch_window") val launchWindow: Int,
    @field:Json(name = "rocket") val rocket: RocketSummary,
    @field:Json(name = "ships") val ships: List<String>,
    @field:Json(name = "telemetry") val telemetry: Telemetry,
    @field:Json(name = "launch_site") val launchSite: LaunchSite,
    @field:Json(name = "launch_success") val launchSuccess: Boolean,
    @field:Json(name = "links") val links: Links,
    @field:Json(name = "details") val details: String,
    @field:Json(name = "upcoming") val upcoming: Boolean,
    @field:Json(name = "static_fire_date_utc") val staticFireDate: Date,
    @field:Json(name = "timeline") val timeline: Timeline
)

