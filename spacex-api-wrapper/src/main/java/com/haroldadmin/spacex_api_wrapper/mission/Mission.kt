package com.haroldadmin.spacex_api_wrapper.mission

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Mission(
    @Json(name = "mission_name") val name: String,
    @Json(name = "mission_id") val id: String,
    @Json(name = "manufacturers") val manufacturers: List<String>,
    @Json(name = "payload_ids") val payloadIds: List<String>,
    @Json(name = "wikipedia") val wikipedia: String,
    @Json(name = "website") val website: String?,
    @Json(name = "twitter") val twitter: String?,
    @Json(name = "description") val description: String?
)