package com.haroldadmin.moonshot.db

import com.squareup.sqldelight.ColumnAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal class ListToStringAdapter : ColumnAdapter<List<String>, String> {
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

internal class LocalDateAdapter: ColumnAdapter<LocalDate, String> {
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
