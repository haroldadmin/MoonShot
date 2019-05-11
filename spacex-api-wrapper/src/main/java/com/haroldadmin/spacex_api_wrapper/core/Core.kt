package com.haroldadmin.spacex_api_wrapper.core

import com.haroldadmin.spacex_api_wrapper.common.MissionSummary
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class Core (
    @Json(name = "core_serial") val serial: String,
    @Json(name = "block") val block: Int?,
    @Json(name = "status") val status: String,
    @Json(name = "original_launch") val originalLaunch: Date?,
    @Json(name = "missions") val missions: List<MissionSummary>,
    @Json(name = "reuse_count") val reuseCount: Int,
    @Json(name = "rtls_attempts") val rtlsAttempts : Int,
    @Json(name = "rtls_landings") val rtlsLandings: Int,
    @Json(name = "asds_attempts") val asdsAttempts: Int,
    @Json(name = "asds_landings") val asdsLandings: Int,
    @Json(name = "water_landing") val waterLanding: Boolean,
    @Json(name = "details") val details: String?
)