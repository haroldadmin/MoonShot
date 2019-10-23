package com.haroldadmin.moonshot.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.OnViewRecycled
import com.airbnb.epoxy.TextProp
import com.google.android.material.card.MaterialCardView
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.utils.asyncText

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TextCard @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrSet, defStyleAttr) {

    init {
        inflate(context, R.layout.card_text, this)
    }

    private val card: MaterialCardView = findViewById(R.id.cardRoot)
    private val header: AppCompatTextView = findViewById(R.id.headerText)
    private val content: AppCompatTextView = findViewById(R.id.contentText)

    @TextProp
    fun setHeader(text: CharSequence) {
        header.asyncText(text)
    }

    @TextProp
    fun setContent(text: CharSequence) {
        content.asyncText(text)
    }

    @CallbackProp
    fun onTextClick(onClick: OnClickListener?) {
        onClick?.let {
            card.apply {
                setOnClickListener(it)
                isClickable = true
            }
        }
    }

    @OnViewRecycled
    fun cleanup() {
        card.apply {
            setOnClickListener(null)
            isClickable = false
        }
    }
}