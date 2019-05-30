package com.haroldadmin.moonshot

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class LaunchItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val titleView: AppCompatTextView
    private val subtitleView: AppCompatTextView

    init {
        inflate(context, R.layout.basic_row, this)
        titleView = findViewById(R.id.title)
        subtitleView = findViewById(R.id.subtitle)
        orientation = VERTICAL
    }

    @TextProp
    fun setTitle(title: CharSequence) {
        titleView.setTextFuture(PrecomputedTextCompat.getTextFuture(title, TextViewCompat.getTextMetricsParams(titleView), null))
    }

    @TextProp
    fun setSubtitle(subtitle: CharSequence?) {
        subtitle?.let {
            subtitleView.visibility = View.VISIBLE
            subtitleView.setTextFuture(PrecomputedTextCompat.getTextFuture(subtitle, TextViewCompat.getTextMetricsParams(subtitleView), null))
        } ?: run {
            subtitleView.visibility = View.GONE
        }
    }

    @CallbackProp
    fun setClickListener(clickListener: OnClickListener?) {
        setOnClickListener(clickListener)
    }
}