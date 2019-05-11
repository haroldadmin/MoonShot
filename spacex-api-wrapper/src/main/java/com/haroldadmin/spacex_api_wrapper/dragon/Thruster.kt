package com.haroldadmin.spacex_api_wrapper.dragon

import com.haroldadmin.spacex_api_wrapper.common.Thrust
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Thruster(
    @Json(name = "type") val type: String,
    @Json(name = "amount") val amount: Int,
    @Json(name = "pods") val pods: Int,
    @Json(name = "fuel_1") val fuel1: String,
    @Json(name = "fuel_2") val fuel2: String,
    @Json(name = "thrust") val thrust: Thrust
)