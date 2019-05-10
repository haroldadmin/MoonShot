package com.haroldadmin.spacex_api_wrapper.history

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Links(
    @field:Json(name = "reddit") val reddit: String?,
    @field:Json(name = "article") val article: String?,
    @field:Json(name = "wikipedia") val wikipedia: String?
)