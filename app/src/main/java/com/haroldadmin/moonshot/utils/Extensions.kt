package com.haroldadmin.moonshot.utils

import android.content.res.Configuration
import android.util.Log
import androidx.core.os.ConfigurationCompat
import com.haroldadmin.moonshot.BuildConfig
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date

fun Date.format(configuration: Configuration, pattern: String = "dd-MM-yyyy"): String {
    val locale = ConfigurationCompat.getLocales(configuration).get(0)
    val formatter = SimpleDateFormat(pattern, locale)
    return formatter.format(this)
}

fun Long.format(configuration: Configuration): String {
    val locale = ConfigurationCompat.getLocales(configuration).get(0)
    val formatter = NumberFormat.getNumberInstance(locale)
    return formatter.format(this)
}

fun Any.log(message: String) {
    if (BuildConfig.DEBUG) Log.d(this::class.java.simpleName, message)
}
