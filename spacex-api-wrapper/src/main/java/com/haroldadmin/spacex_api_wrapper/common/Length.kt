package com.haroldadmin.spacex_api_wrapper.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Length(
    @Json(name = "meters") val meters: Double?,
    @Json(name = "feet") val feet: Double?
)