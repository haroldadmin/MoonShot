package com.haroldadmin.moonshot.launchDetails.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import coil.api.load
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.github.chrisbanes.photoview.PhotoView
import com.haroldadmin.moonshot.launchDetails.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_MATCH_HEIGHT)
class FullscreenPhotoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.item_fullscreen_photo, this)
    }

    private val photoView: PhotoView = findViewById(R.id.photoView)

    @ModelProp
    fun setUrl(url: String?) {
        photoView.load(url) {
            error(R.drawable.ic_round_photo_24)
            placeholder(R.drawable.ic_round_photo_24)
            crossfade(true)
        }
    }

}