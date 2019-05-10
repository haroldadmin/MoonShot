package com.haroldadmin.spacex_api_wrapper.info

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Headquarters(
    @field:Json(name = "address") val address: String,
    @field:Json(name = "city") val city: String,
    @field:Json(name = "state") val state: String
)