package com.haroldadmin.spacex_api_wrapper.launchpad

import com.haroldadmin.spacex_api_wrapper.common.Location
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LaunchPad (
    @Json(name = "id") val id: Int,
    @Json(name = "status") val status: String,
    @Json(name = "location") val location: Location,
    @Json(name = "vehicles_launched") val vehiclesLaunched: List<String>,
    @Json(name = "attempted_launches") val attemptedLaunches: Int,
    @Json(name = "successful_launches") val successfulLaunches: Int,
    @Json(name = "wikipedia") val wikipedia: String,
    @Json(name = "details") val details: String,
    @Json(name = "site_id") val siteId: String,
    @Json(name = "site_name_long") val siteNameLong: String
)