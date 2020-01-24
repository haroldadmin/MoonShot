package com.haroldadmin.spacex_api_wrapper.launches

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LaunchSite(
    @Json(name = "site_id") val id: String?,
    @Json(name = "site_name") val name: String?,
    @Json(name = "site_name_long") val nameLong: String?
)