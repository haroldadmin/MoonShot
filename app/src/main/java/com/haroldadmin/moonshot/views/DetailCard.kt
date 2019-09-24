package com.haroldadmin.moonshot.views

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
import com.airbnb.epoxy.TextProp
import com.google.android.material.card.MaterialCardView
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.utils.asyncText

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class DetailCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.card_detail, this)
    }

    private val card: MaterialCardView = findViewById(R.id.cardRoot)
    private val header: AppCompatTextView = findViewById(R.id.headerText)
    private val content: AppCompatTextView = findViewById(R.id.contentText)
    private val icon: ImageView = findViewById(R.id.detailIcon)

    @TextProp
    fun setHeader(text: CharSequence) {
        header.asyncText(text)
    }

    @TextProp
    fun setContent(text: CharSequence) {
        content.asyncText(text)
    }

    @ModelProp
    fun setIcon(@DrawableRes res: Int) {
        icon.load(res)
    }

    @CallbackProp
    fun setOnDetailClick(onClick: OnClickListener?) {
        card.setOnClickListener(onClick)
    }
}