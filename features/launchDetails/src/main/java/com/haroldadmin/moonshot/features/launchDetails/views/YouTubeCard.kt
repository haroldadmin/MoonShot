package com.haroldadmin.moonshot.features.launchDetails.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import coil.api.load
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.OnViewRecycled
import com.google.android.material.card.MaterialCardView
import com.haroldadmin.moonshot.features.launchDetails.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class YouTubeCard @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrSet, defStyleAttr) {

    init {
        inflate(context, R.layout.card_youtube, this)
    }

    private val card: MaterialCardView = findViewById(R.id.cardRoot)
    private val background: ImageView = findViewById(R.id.backgroundImage)
    private val label: AppCompatTextView = findViewById(R.id.labelText)

    @ModelProp
    fun setThumbnailUrl(url: String?) {
        background.load(url) {
            error(R.drawable.ic_round_ondemand_video_24px)
            placeholder(R.drawable.ic_round_ondemand_video_24px)
        }
    }

    @CallbackProp
    fun setOnYoutubeClick(onClick: OnClickListener?) {
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