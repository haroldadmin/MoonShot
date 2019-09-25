package com.haroldadmin.moonshot.launchPad.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import coil.api.load
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.OnViewRecycled
import com.google.android.material.card.MaterialCardView
import com.haroldadmin.moonshot.launchPad.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class MapCard @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null,
    defStyleAttr: Int = 0
): FrameLayout(context, attrSet, defStyleAttr) {

    init {
        inflate(context, R.layout.card_map, this)
    }

    private val card: MaterialCardView = findViewById(R.id.cardRoot)
    private val map: ImageView = findViewById(R.id.map)

    @ModelProp
    fun setMapImageUrl(url: String) {
        map.load(url) {
            error(R.drawable.ic_map_not_found)
        }
    }

    @CallbackProp
    fun setOnMapClick(onClick: OnClickListener?) {
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