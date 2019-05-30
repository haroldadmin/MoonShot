package com.haroldadmin.spacex_api_wrapper.launches

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RocketSummary(
    @Json(name = "rocket_id") val rocketId: String,
    @Json(name = "rocket_name") val name: String,
    @Json(name = "rocket_type") val type: String,
    @Json(name = "first_stage") val firstStage: FirstStageSummary,
    @Json(name = "second_stage") val secondState: SecondStageSummary,
    @Json(name = "fairings") val fairing: Fairing?
)