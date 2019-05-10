package com.haroldadmin.spacex_api_wrapper.launchpad

import com.haroldadmin.spacex_api_wrapper.common.Location
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LaunchPad (
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "status") val status: String,
    @field:Json(name = "location") val location: Location,
    @field:Json(name = "vehicles_launched") val vehiclesLaunched: List<String>,
    @field:Json(name = "attempted_launches") val attemptedLaunches: Int,
    @field:Json(name = "successful_launches") val successfulLaunches: Int,
    @field:Json(name = "wikipedia") val wikipedia: String,
    @field:Json(name = "details") val details: String,
    @field:Json(name = "site_id") val sitId: String,
    @field:Json(name = "site_name_long") val siteNameLong: String
)