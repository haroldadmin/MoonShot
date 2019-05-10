package com.haroldadmin.spacex_api_wrapper.launches

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SecondStageSummary(
    @field:Json(name = "block") val block: Int,
    @field:Json(name = "payloads") val payloads: List<Payload>
)