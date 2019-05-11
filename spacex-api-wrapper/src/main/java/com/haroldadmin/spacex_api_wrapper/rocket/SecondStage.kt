package com.haroldadmin.spacex_api_wrapper.rocket

import com.haroldadmin.spacex_api_wrapper.common.Thrust
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SecondStage(
    @Json(name = "engines") val engines: Int,
    @Json(name = "fuel_amount_tons") val fuelAmountTons: Int,
    @Json(name = "burn_time_sec") val burnTimeSecs: Int,
    @Json(name = "thrust") val thrust: Thrust,
    @Json(name = "payloads") val payloads: Payloads
)