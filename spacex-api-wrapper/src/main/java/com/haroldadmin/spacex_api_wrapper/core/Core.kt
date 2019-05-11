package com.haroldadmin.spacex_api_wrapper.core

import com.haroldadmin.spacex_api_wrapper.common.MissionSummary
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class Core (
    @field:Json(name = "core_serial") val serial: String,
    @field:Json(name = "block") val block: Int?,
    @field:Json(name = "status") val status: String,
    @field:Json(name = "original_launch") val originalLaunch: Date?,
    @field:Json(name = "missions") val missions: List<MissionSummary>,
    @field:Json(name = "reuse_count") val reuseCount: Int,
    @field:Json(name = "rtls_attempts") val rtlsAttempts : Int,
    @field:Json(name = "rtls_landings") val rtlsLandings: Int,
    @field:Json(name = "asds_attempts") val asdsAttempts: Int,
    @field:Json(name = "asds_landings") val asdsLandings: Int,
    @field:Json(name = "water_landing") val waterLanding: Boolean,
    @field:Json(name = "details") val details: String
)