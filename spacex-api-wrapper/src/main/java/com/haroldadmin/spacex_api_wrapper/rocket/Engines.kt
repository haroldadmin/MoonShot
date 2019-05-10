package com.haroldadmin.spacex_api_wrapper.rocket

import com.haroldadmin.spacex_api_wrapper.common.Thrust
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Engines(
    @field:Json(name = "number") val number: Int,
    @field:Json(name = "type") val type: String,
    @field:Json(name = "version") val version: String,
    @field:Json(name = "layout") val layout: String,
    @field:Json(name = "engine_loss_max") val engineLossMax: Int,
    @field:Json(name = "propellant_1") val propellant1: String,
    @field:Json(name = "propellant_2") val propellant2: String,
    @field:Json(name = "thrust_sea_level") val thrustSeaLevel: Thrust,
    @field:Json(name = "thrust_vacuum") val thrustVacuum: Thrust,
    @field:Json(name = "thrust_to_weight") val thrustToWeightRatio: Double
)