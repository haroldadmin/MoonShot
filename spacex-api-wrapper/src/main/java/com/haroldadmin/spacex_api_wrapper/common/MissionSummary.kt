package com.haroldadmin.spacex_api_wrapper.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MissionSummary(
    @Json(name = "name") val name: String,
    @Json(name = "flight") val flight: Int
)