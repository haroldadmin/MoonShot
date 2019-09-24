package com.haroldadmin.moonshot.utils

import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.os.ConfigurationCompat
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import com.haroldadmin.moonshot.BuildConfig
import com.haroldadmin.moonshot.models.SHORT_DATE_FORMAT
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Timer
import java.util.concurrent.Executor
import kotlin.concurrent.fixedRateTimer

fun Date.format(configuration: Configuration, pattern: String = SHORT_DATE_FORMAT): String {
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

fun countdownTimer(
    duration: Long,
    period: Long = 1000L,
    onFinish: (() -> Unit)? = null,
    action: (timeLeft: Long) -> Unit
): Timer {
    var currentTime = 0L
    return fixedRateTimer(name = "CountDown Timer", period = period) {
        if (currentTime >= duration) {
            action.invoke(0L)
            onFinish?.invoke()
            cancel()
        } else {
            action.invoke(duration - currentTime)
            currentTime += period
        }
    }
}

fun AppCompatTextView.asyncText(text: CharSequence, executor: Executor? = null) {
    this.setTextFuture(
        PrecomputedTextCompat.getTextFuture(text, TextViewCompat.getTextMetricsParams(this), executor)
    )
}