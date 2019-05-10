package com.haroldadmin.spacex_api_wrapper.launches

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LaunchSite(
    @field:Json(name = "site_id") val id: String,
    @field:Json(name = "site_name") val name: String,
    @field:Json(name = "site_name_long") val nameLong: String
)