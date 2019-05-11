package com.haroldadmin.spacex_api_wrapper.launches

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Fairing(
    @field:Json(name = "reused") val reused: Boolean,
    @field:Json(name = "recovery_attempt") val recoveryAttempt: Boolean,
    @field:Json(name = "recovered") val recovered: Boolean,
    @field:Json(name = "ship") val ship: String?
)