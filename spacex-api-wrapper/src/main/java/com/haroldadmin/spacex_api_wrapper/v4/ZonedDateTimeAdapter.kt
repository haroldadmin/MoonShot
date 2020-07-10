package com.haroldadmin.spacex_api_wrapper.v4

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException

internal class ZonedDateTimeAdapter {

    @Suppress("NewApi")
    @FromJson
    fun fromJson(json: String): ZonedDateTime? {
        return try {
            ZonedDateTime.parse(json)
        } catch(ex: DateTimeParseException) {
            return null
        }
    }

    @ToJson
    fun toJson(dateTime: ZonedDateTime): String {
        return dateTime.toString()
    }

}