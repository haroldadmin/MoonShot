package com.haroldadmin.spacex_api_wrapper.launches

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoreSummary(
    @Json(name = "core_serial") val serial: String?,
    @Json(name = "flight") val flight: Int?,
    @Json(name = "block") val block: Int?,
    @Json(name = "gridfins") val gridfins: Boolean?,
    @Json(name = "legs") val legs: Boolean?,
    @Json(name = "reused") val reused: Boolean?,
    @Json(name = "land_success") val landSuccess: Boolean?,
    @Json(name = "landing_intent") val landingIntent: Boolean?,
    @Json(name = "landing_type") val landingType: String?,
    @Json(name = "landing_vehicle") val landingVehicle: String?
)