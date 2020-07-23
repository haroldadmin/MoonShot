package com.haroldadmin.moonshot.db

import com.squareup.sqldelight.ColumnAdapter
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ListToStringAdapter : ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String): List<String> {
        if (databaseValue == "") {
            return emptyList()
        }
        return databaseValue.split(',')
    }

    override fun encode(value: List<String>): String {
        return value.joinToString(",")
    }
}

class LocalDateAdapter: ColumnAdapter<LocalDate, String> {
    override fun decode(databaseValue: String): LocalDate {
        if (databaseValue.isBlank()) {
            return LocalDate.of(1970, 1, 1)
        }
        val format = DateTimeFormatter.ISO_LOCAL_DATE
        return LocalDate.parse(databaseValue, format)
    }

    override fun encode(value: LocalDate): String {
        return value.toString()
    }

}

class ZonedDateTimeAdapter: ColumnAdapter<ZonedDateTime, String> {

    override fun decode(databaseValue: String): ZonedDateTime {
        return ZonedDateTime.parse(databaseValue)
    }

    override fun encode(value: ZonedDateTime): String {
        return value.toString()
    }
}

class ListToIntAdapter: ColumnAdapter<List<Int>, String> {
    override fun decode(databaseValue: String): List<Int> {
        if (databaseValue == "") {
            return emptyList()
        }
        return databaseValue.split(',').map { i -> i.toInt() }
    }

    override fun encode(value: List<Int>): String {
        return value.joinToString(",")
    }
}