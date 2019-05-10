package com.haroldadmin.spacex_api_wrapper.landingpads

import com.haroldadmin.spacex_api_wrapper.common.Location
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LandingPad (
    @field:Json(name = "id") val id: String,
    @field:Json(name = "full_name") val fullName: String,
    @field:Json(name = "status") val status: String,
    @field:Json(name = "location") val location: Location,
    @field:Json(name = "landing_type") val landingType: String,
    @field:Json(name = "attempted_landings") val attemptedLandings: Int,
    @field:Json(name = "successful_landings") val successfulLandings: Int,
    @field:Json(name = "wikipedia") val wikipedia: String,
    @field:Json(name = "details") val details: String
)