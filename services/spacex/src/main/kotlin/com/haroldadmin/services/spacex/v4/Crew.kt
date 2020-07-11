package com.haroldadmin.services.spacex.v4

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

enum class CrewStatus {
    active, inactive, retired, unknown
}

@JsonClass(generateAdapter = true)
data class Crew(
    @Json(name = "name") val name: String?,
    @Json(name = "status") val status: CrewStatus,
    @Json(name = "agency") val agency: String?,
    @Json(name = "image") val image: String?,
    @Json(name = "wikipedia") val wikipedia: String?,
    @Json(name = "launches") val launchIDs: List<String>
)
