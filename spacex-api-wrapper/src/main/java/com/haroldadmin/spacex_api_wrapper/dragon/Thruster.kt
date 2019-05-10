package com.haroldadmin.spacex_api_wrapper.dragon

import com.haroldadmin.spacex_api_wrapper.common.Thrust
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Thruster(
    @field:Json(name = "type") val type: String,
    @field:Json(name = "amount") val amount: Int,
    @field:Json(name = "pods") val pods: Int,
    @field:Json(name = "fuel_1") val fuel1: String,
    @field:Json(name = "fuel_2") val fuel2: String,
    @field:Json(name = "thrust") val thrust: Thrust
)