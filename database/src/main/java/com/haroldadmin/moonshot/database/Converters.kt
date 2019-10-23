package com.haroldadmin.moonshot.database

import androidx.room.TypeConverter
import com.haroldadmin.moonshot.models.DatePrecision
import java.util.Date

class Converters {

    // Date Converter
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // List<String> Converter
    @TypeConverter
    fun stringToListOfString(value: String?): List<String> {
        return value.takeIf { !it.isNullOrBlank() }
            ?.split(",")
            ?.fold(listOf()) { list, s ->
                list + s
            } ?: listOf()
    }

    @TypeConverter
    fun listOfStringToString(value: List<String>?): String {
        return value?.joinToString(separator = ",") ?: ""
    }

    // List<Int> Converter for IDs
    @TypeConverter
    fun stringToListOfInt(value: String?): List<Int>? {
        val list = mutableListOf<Int>()
        value.takeIf { !it.isNullOrBlank() }
            ?.split(",")
            ?.map { id: String ->
                list.add(id.toInt())
            }
        return list
    }

    @TypeConverter
    fun listOfIntToString(value: List<Int>?): String? {
        return value?.joinToString(separator = ",")
    }

    @TypeConverter
    fun datePrecisionToString(datePrecision: DatePrecision): String? {
        return datePrecision.name
    }

    @TypeConverter
    fun stringToDatePrecision(s: String): DatePrecision? {
        return try {
            DatePrecision.valueOf(s)
        } catch (ex: IllegalArgumentException) {
            null
        }
    }
}