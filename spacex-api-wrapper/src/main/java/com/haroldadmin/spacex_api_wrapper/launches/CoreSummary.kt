package com.haroldadmin.spacex_api_wrapper.launches

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoreSummary(
    @field:Json(name = "core_serial") val serial: String,
    @field:Json(name = "flight") val flight: Int,
    @field:Json(name = "block") val block: Int?,
    @field:Json(name = "gridfins") val gridfins: Boolean,
    @field:Json(name = "legs") val legs: Boolean,
    @field:Json(name = "reused") val reused: Boolean,
    @field:Json(name = "land_success") val landSuccess: Boolean?,
    @field:Json(name = "landing_intent") val landingIntent: Boolean,
    @field:Json(name = "landing_type") val landingType: String?,
    @field:Json(name = "landing_vehicle") val landingVehicle: String?
)