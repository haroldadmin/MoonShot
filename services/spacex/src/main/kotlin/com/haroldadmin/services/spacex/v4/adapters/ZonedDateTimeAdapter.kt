package com.haroldadmin.services.spacex.v4.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
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