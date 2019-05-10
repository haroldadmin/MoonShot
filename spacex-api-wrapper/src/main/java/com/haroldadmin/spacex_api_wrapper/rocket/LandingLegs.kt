package com.haroldadmin.spacex_api_wrapper.rocket

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LandingLegs(
    @field:Json(name = "number") val number: Int,
    @field:Json(name = "material") val material: String
)