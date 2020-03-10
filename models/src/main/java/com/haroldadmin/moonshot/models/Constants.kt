package com.haroldadmin.moonshot.models

// Using lower case names because Moshi uses name() to match enum types
enum class DatePrecision(val dateFormat: String) {
    year("yyyy"),
    half("yyyy"),
    quarter("yyyy"),
    month("MMM, yyyy"),
    day("d MMM, yyyy"),
    hour("hh:mm a, d MMM, yyyy"),
    unknown("'Unknown'")
}

fun String?.toDatePrecision(): DatePrecision {
    this ?: return DatePrecision.unknown
    return try {
        DatePrecision.valueOf(this)
    } catch (ex: IllegalArgumentException) {
        DatePrecision.unknown
    }
}
