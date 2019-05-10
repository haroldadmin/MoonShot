package com.haroldadmin.spacex_api_wrapper.launches

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RocketSummary(
    @field:Json(name = "rocket_id") val id: String,
    @field:Json(name = "rocket_name") val name: String,
    @field:Json(name = "rocket_type") val type: String,
    @field:Json(name = "first_stage") val firstStage: FirstStageSummary,
    @field:Json(name = "second_stage") val secondState: SecondStageSummary,
    @field:Json(name = "fairings") val fairing: List<Fairing>
)