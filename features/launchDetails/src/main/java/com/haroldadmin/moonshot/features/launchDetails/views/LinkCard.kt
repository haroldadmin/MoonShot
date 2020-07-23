package com.haroldadmin.moonshot.features.launchDetails.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.OnViewRecycled
import com.airbnb.epoxy.TextProp
import com.google.android.material.card.MaterialCardView
import com.haroldadmin.moonshot.features.launchDetails.R
import com.haroldadmin.moonshot.utils.asyncText

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class LinkCard @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrSet, defStyleAttr) {

    init {
        inflate(context, R.layout.card_link, this)
    }

    private val card: MaterialCardView = findViewById(R.id.cardRoot)
    private val gradientContainer: FrameLayout = findViewById(R.id.gradient)
    private val title: AppCompatTextView = findViewById(R.id.linkText)

    @TextProp
    fun setTitle(text: CharSequence) {
        title.asyncText(text)
    }

    @ModelProp
    fun setGradient(@DrawableRes res: Int) {
        gradientContainer.setBackgroundResource(res)
    }

    @CallbackProp
    fun setOnLinkClick(onClick: OnClickListener?) {
        onClick?.let { clickListener ->
            card.apply {
                setOnClickListener(clickListener)
                isClickable = true
            }
        } ?: run {
            isClickable = false
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