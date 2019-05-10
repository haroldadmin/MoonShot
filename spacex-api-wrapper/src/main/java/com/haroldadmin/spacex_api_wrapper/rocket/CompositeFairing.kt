package com.haroldadmin.spacex_api_wrapper.rocket

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import sun.security.util.Length

@JsonClass(generateAdapter = true)
data class CompositeFairing(
    @field:Json(name = "height") val height: Length,
    @field:Json(name = "diameter") val diameter: Length
)