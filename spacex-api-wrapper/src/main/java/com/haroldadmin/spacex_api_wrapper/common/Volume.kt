package com.haroldadmin.spacex_api_wrapper.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Volume(
    @field:Json(name = "cubic_meters") val cubicMeters: Double,
    @field:Json(name = "cubic_feet") val cubicFeet: Double
)