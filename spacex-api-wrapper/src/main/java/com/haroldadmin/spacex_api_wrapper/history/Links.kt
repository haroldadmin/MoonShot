package com.haroldadmin.spacex_api_wrapper.history

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Links(
    @Json(name = "reddit") val reddit: String?,
    @Json(name = "article") val article: String?,
    @Json(name = "wikipedia") val wikipedia: String?
)