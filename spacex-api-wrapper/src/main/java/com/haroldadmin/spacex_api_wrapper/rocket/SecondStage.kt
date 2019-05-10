package com.haroldadmin.spacex_api_wrapper.rocket

import com.haroldadmin.spacex_api_wrapper.common.Thrust
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SecondStage(
    @field:Json(name = "engines") val engines: Int,
    @field:Json(name = "fuel_amount_tons") val fuelAmountTons: Int,
    @field:Json(name = "burn_time_sec") val burnTimeSecs: Int,
    @field:Json(name = "thrust") val thrust: Thrust,
    @field:Json(name = "payloads") val payloads: Payloads
)