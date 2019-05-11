package com.haroldadmin.spacex_api_wrapper.launches

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SecondStageSummary(
    @Json(name = "block") val block: Int,
    @Json(name = "payloads") val payloads: List<Payload>
)