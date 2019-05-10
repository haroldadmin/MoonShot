package com.haroldadmin.spacex_api_wrapper.capsule

import com.haroldadmin.spacex_api_wrapper.common.MissionSummary
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class Capsule(
    @field:Json(name = "capsule_serial") val serial: String,
    @field:Json(name = "capsule_id") val id: String,
    @field:Json(name = "status") val status: String,
    @field:Json(name = "original_launch") val originalLaunch: Date,
    @field:Json(name = "missions") val missions: List<MissionSummary>,
    @field:Json(name = "landings") val landings: Int,
    @field:Json(name = "type") val type: String,
    @field:Json(name = "details") val details: String?,
    @field:Json(name = "reuse_count") val reuseCount: Int
)