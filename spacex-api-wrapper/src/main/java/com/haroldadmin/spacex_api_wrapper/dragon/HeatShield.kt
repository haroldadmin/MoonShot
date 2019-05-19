package com.haroldadmin.spacex_api_wrapper.dragon

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HeatShield(
    @Json(name = "material") val material: String,
    @Json(name = "size_meters") val sizeMeters: Double,
    @Json(name = "temp_degrees") val tempDegrees: Double,
    @Json(name = "dev_partner") val devPartner: String
)