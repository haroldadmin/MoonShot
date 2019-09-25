package com.haroldadmin.moonshot.launchDetails.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import coil.api.load
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.google.android.material.card.MaterialCardView
import com.haroldadmin.moonshot.launchDetails.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class PictureCard @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null,
    defStyleAttr: Int = 0
): FrameLayout(context, attrSet, defStyleAttr) {

    init {
        inflate(context, R.layout.card_picture, this)
    }

    private val card: MaterialCardView = findViewById(R.id.cardRoot)
    private val image: ImageView = findViewById(R.id.image)

    @ModelProp
    fun setImageUrl(url: String) {
        image.load(url)
    }
}