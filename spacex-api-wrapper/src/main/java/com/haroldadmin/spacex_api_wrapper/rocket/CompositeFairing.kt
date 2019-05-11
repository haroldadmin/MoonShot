package com.haroldadmin.spacex_api_wrapper.rocket

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import sun.security.util.Length

@JsonClass(generateAdapter = true)
data class CompositeFairing(
    @Json(name = "height") val height: Length,
    @Json(name = "diameter") val diameter: Length
)