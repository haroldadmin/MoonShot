package com.haroldadmin.spacex_api_wrapper.v4

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LandingPad(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String?,
    @Json(name = "full_name") val fullName: String?,
    @Json(name = "status") val status: String?,
    @Json(name = "type") val type: String?,
    @Json(name = "locality") val locality: String?,
    @Json(name = "latitude") val latitude: Double?,
    @Json(name = "longitude") val longitude: Double?,
    @Json(name = "landing_attempts") val landingAttempts: Int?,
    @Json(name = "landing_successes") val landingSuccesses: Int?,
    @Json(name = "wikipedia") val wikipedia: String?,
    @Json(name = "details") val details: String?,
    @Json(name = "launches") val launchIDs: List<String>
)

