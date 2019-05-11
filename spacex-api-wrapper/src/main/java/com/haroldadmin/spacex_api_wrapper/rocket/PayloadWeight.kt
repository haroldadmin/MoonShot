package com.haroldadmin.spacex_api_wrapper.rocket

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PayloadWeight(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "kg") val weightKg: Double,
    @Json(name = "lb") val weightLb: Double
)