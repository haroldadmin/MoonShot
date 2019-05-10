package com.haroldadmin.spacex_api_wrapper.dragon

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HeatShield(
    @field:Json(name = "material") val material: String,
    @field:Json(name = "size_meters") val sizeMeters: Int,
    @field:Json(name = "temp_degrees") val tempDegrees: Int,
    @field:Json(name = "dev_partner") val devPartner: String
)