package com.haroldadmin.moonshot.features.launches.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.OnViewRecycled
import com.airbnb.epoxy.TextProp
import com.haroldadmin.moonshot.features.launches.R
import com.haroldadmin.moonshot.utils.asyncText

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class FilterOptionView @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrSet, defStyleAttr) {

    init {
        inflate(context, R.layout.view_filter_option, this)
    }

    private val filter: AppCompatTextView = findViewById(R.id.filter)

    @TextProp
    fun setName(name: CharSequence) {
        filter.asyncText(name)
    }

    @CallbackProp
    fun setOnFilterSelected(onClick: OnClickListener?) {
        onClick?.let { clickListener ->
            rootView.setOnClickListener(clickListener)
        }
    }

    @OnViewRecycled
    fun cleanup() {
        rootView.setOnClickListener(null)
    }
}