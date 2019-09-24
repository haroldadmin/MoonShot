package com.haroldadmin.moonshot.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.utils.asyncText

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class LoadingView @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null,
    defStyleAttr: Int = 0
): FrameLayout(context, attrSet, defStyleAttr) {

    init {
        inflate(context, R.layout.view_loading, this)
    }

    private val loadingText: AppCompatTextView = findViewById(R.id.loadingText)

    @TextProp
    fun setLoadingText(text: CharSequence) {
        loadingText.asyncText(text)
    }
}