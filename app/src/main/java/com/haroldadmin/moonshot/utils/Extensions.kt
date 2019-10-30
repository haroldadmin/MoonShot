package com.haroldadmin.moonshot.utils

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.os.ConfigurationCompat
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.lifecycle.viewModelScope
import coil.api.load
import coil.request.LoadRequestBuilder
import com.haroldadmin.moonshot.BuildConfig
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.models.DatePrecision
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import java.util.Timer
import java.util.concurrent.Executor
import kotlin.concurrent.fixedRateTimer

fun Context.formatNumber(number: Long): String {
    val locale = ConfigurationCompat.getLocales(resources.configuration)[0]
    val formatter = NumberFormat.getInstance(locale)
    return formatter.format(number)
}

fun Date.toLocalDateTime(): DateTime {
    val utc = DateTime(this, DateTimeZone.UTC)
    val currentTimeZone = DateTimeZone.forTimeZone(TimeZone.getDefault())
    return DateTime(utc, currentTimeZone)
}

fun Context.formatDate(date: Date, pattern: String = DatePrecision.day.dateFormat): String {
    val locale = ConfigurationCompat.getLocales(resources.configuration)[0]
    val formatter = SimpleDateFormat(pattern, locale)
    return formatter.format(date)
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
    asyncText(textProducer(), executor)
}

// https://github.com/coil-kt/coil/issues/61
inline fun ImageView.loadNullable(url: String?, @DrawableRes errorRes: Int, builder: LoadRequestBuilder.() -> Unit = {}) {
    if (url == null) {
        load(errorRes)
    } else {
        load(url, builder = builder)
    }
}

inline fun <T : MoonShotViewModel<*>> T.launchAfterDelay(delayMs: Long, crossinline action: suspend T.() -> Unit) {
    viewModelScope.launch {
        delay(delayMs)
        action()
    }
}