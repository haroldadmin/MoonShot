package com.haroldadmin.spacex_api_wrapper.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Mass(
    @Json(name = "kg") val kg: Double,
    @Json(name = "lb") val lb: Double
)