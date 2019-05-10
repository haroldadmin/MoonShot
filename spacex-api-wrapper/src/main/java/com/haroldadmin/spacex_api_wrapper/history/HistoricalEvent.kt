package com.haroldadmin.spacex_api_wrapper.history

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class HistoricalEvent(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "event_date_utc") val date: Date,
    @field:Json(name = "flight_number") val flightNumber: Int,
    @field:Json(name = "details") val details: String,
    @field:Json(name = "links") val links: Links
)