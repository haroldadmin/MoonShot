package com.haroldadmin.spacex_api_wrapper.mission

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Mission (
    @field:Json(name = "mission_name") val name: String,
    @field:Json(name = "mission_id") val id: String,
    @field:Json(name = "manufacturers") val manufacturers: List<String>,
    @field:Json(name = "payload_ids") val payloadIds: List<String>,
    @field:Json(name = "wikipedia") val wikipedia: String,
    @field:Json(name = "website") val website: String,
    @field:Json(name = "twitter") val twitter: String,
    @field:Json(name = "description") val description: String
)