package com.haroldadmin.moonshot.utils

import android.content.res.Configuration
import android.util.Log
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.os.ConfigurationCompat
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import coil.api.load
import coil.request.LoadRequestBuilder
import com.haroldadmin.moonshot.BuildConfig
import com.haroldadmin.moonshot.models.DatePrecision
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Timer
import java.util.concurrent.Executor
import kotlin.concurrent.fixedRateTimer

fun Date.format(configuration: Configuration, pattern: String = DatePrecision.day.dateFormat): String {
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

inline fun AppCompatTextView.asyncText(executor: Executor? = null, crossinline textProducer: () -> CharSequence) {
    this.setTextFuture(
        PrecomputedTextCompat.getTextFuture(textProducer(), TextViewCompat.getTextMetricsParams(this), executor)
    )
}

// https://github.com/coil-kt/coil/issues/61
inline fun ImageView.loadNullable(url: String?, @DrawableRes errorRes: Int, builder: LoadRequestBuilder.() -> Unit = {}) {
    if (url == null) {
        load(errorRes)
    } else {
        load(url, builder = builder)
    }
}
