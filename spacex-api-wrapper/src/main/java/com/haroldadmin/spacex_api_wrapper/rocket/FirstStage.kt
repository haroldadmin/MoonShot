package com.haroldadmin.spacex_api_wrapper.rocket

import com.haroldadmin.spacex_api_wrapper.common.Thrust
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FirstStage(
    @field:Json(name = "reusable") val reusable: Boolean,
    @field:Json(name = "engines") val engines: Int,
    @field:Json(name = "fuel_amount_tons") val fuelAmountTons: Int,
    @field:Json(name = "burn_time_sec") val burnTimeSecs: Int,
    @field:Json(name = "thrust_sea_level") val thrustSeaLevel: Thrust,
    @field:Json(name = "thrust_vacuum") val thrustVacuum: Thrust
)