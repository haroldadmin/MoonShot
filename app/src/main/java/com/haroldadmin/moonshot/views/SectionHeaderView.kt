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
class SectionHeaderView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defaultStyleAttr) {

    init {
        inflate(context, R.layout.view_section_header, this)
    }

    private val header: AppCompatTextView = findViewById(R.id.headerText)

    @TextProp
    fun setHeader(text: CharSequence) {
        header.asyncText(text)
    }
}