package com.haroldadmin.spacex_api_wrapper.rocket

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PayloadWeight(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "kg") val weightKg: Double,
    @field:Json(name = "lb") val weightLb: Double
)