package com.haroldadmin.spacex_api_wrapper.landingpads

import com.haroldadmin.spacex_api_wrapper.common.Location
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LandingPad(
    @Json(name = "id") val id: String,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "status") val status: String,
    @Json(name = "location") val location: Location,
    @Json(name = "landing_type") val landingType: String,
    @Json(name = "attempted_landings") val attemptedLandings: Int,
    @Json(name = "successful_landings") val successfulLandings: Int,
    @Json(name = "wikipedia") val wikipedia: String,
    @Json(name = "details") val details: String
)