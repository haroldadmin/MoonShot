package com.haroldadmin.spacex_api_wrapper.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Mass(
    @field:Json(name = "kg") val kg: Int,
    @field:Json(name = "lb") val lb: Int
)