package com.haroldadmin.moonshot.about.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import coil.api.load
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.OnViewRecycled
import com.airbnb.epoxy.TextProp
import com.google.android.material.card.MaterialCardView
import com.haroldadmin.moonshot.utils.asyncText
import com.haroldadmin.moonshot.about.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class AboutDetailCard @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null,
    defStyleAttr: Int = 0
): FrameLayout(context, attrSet, defStyleAttr) {

    init {
        inflate(context, R.layout.card_about_detail, this)
    }

    private val card: MaterialCardView = findViewById(R.id.cardRoot)
    private val icon: ImageView = findViewById(R.id.ivIcon)
    private val header: AppCompatTextView = findViewById(R.id.tvHeader)
    private val message: AppCompatTextView = findViewById(R.id.tvMessage)

    @ModelProp
    fun setIcon(@DrawableRes res: Int) {
        icon.load(res)
    }

    @TextProp
    fun setHeader(text: CharSequence) {
        header.asyncText(text)
    }

    @TextProp
    fun setMessage(text: CharSequence) {
        message.asyncText(text)
    }

    @CallbackProp
    fun setOnDetailClick(onClick: OnClickListener?) {
        onClick?.let { clickListener ->
            card.apply {
                setOnClickListener(clickListener)
                isClickable = true
            }
        } ?: run {
            card.isClickable = false
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