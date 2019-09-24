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
class ErrorView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
): FrameLayout(context, attributeSet, defStyleAttr) {

    init {
        inflate(context, R.layout.view_error, this)
    }

    private val errorText: AppCompatTextView = findViewById(R.id.errorText)

    @TextProp
    fun setErrorText(text: CharSequence) {
        errorText.asyncText(text)
    }
}