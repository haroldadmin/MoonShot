package com.haroldadmin.spacex_api_wrapper.history

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class HistoricalEvent(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "event_date_utc") val date: Date,
    @Json(name = "flight_number") val flightNumber: Int?,
    @Json(name = "details") val details: String,
    @Json(name = "links") val links: Links
)