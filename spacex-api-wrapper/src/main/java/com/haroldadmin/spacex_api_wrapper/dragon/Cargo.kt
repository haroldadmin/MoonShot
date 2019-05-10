package com.haroldadmin.spacex_api_wrapper.dragon

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Cargo(
    @field:Json(name = "solar_array") val solarArray: Int,
    @field:Json(name = "unpressurized_cargo") val unpressurizedCargo: Boolean
)