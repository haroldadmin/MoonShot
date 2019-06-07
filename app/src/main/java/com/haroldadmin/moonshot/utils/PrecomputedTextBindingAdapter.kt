package com.haroldadmin.moonshot.utils

import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter

@BindingAdapter(
    "asyncText",
    "android:textSize",
    requireAll = false)
fun asyncText(view: TextView, text: CharSequence, textSize: Int?) {
    // first, set all measurement affecting properties of the text
    // (size, locale, typeface, direction, etc)
    if (textSize != null) {
        // interpret the text size as SP
        view.textSize = textSize.toFloat()
    }
    (view as AppCompatTextView).setTextFuture(
        PrecomputedTextCompat.getTextFuture(text, TextViewCompat.getTextMetricsParams(view), null))
}