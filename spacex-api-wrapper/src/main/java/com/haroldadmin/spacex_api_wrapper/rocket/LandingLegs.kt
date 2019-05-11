package com.haroldadmin.spacex_api_wrapper.rocket

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LandingLegs(
    @Json(name = "number") val number: Int,
    @Json(name = "material") val material: String
)