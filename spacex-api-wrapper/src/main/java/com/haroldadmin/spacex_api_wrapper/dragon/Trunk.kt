package com.haroldadmin.spacex_api_wrapper.dragon

import com.haroldadmin.spacex_api_wrapper.common.Volume
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Trunk(
    @field:Json(name = "trunk_volume") val trunkVolume: Volume,
    @field:Json(name = "cargo") val cargo: Cargo
)