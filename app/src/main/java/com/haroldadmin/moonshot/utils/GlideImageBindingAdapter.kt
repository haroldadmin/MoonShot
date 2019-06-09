package com.haroldadmin.moonshot.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

@BindingAdapter(
    "glideImage",
    "glideImageError",
    "glideImageFallback",
    "glideCircleCrop",
    "glideCrossFade",
    requireAll = false
)
fun loadImage(
    view: ImageView,
    glideImage: String?,
    glideImageError: Drawable?,
    glideImageFallback: Drawable?,
    glideCircleCrop: Boolean?,
    glideCrossFade: Boolean?
) {
    val request = GlideApp.with(view)
        .load(glideImage)
        .error(glideImageError)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .fallback(glideImageFallback)

    if (glideCircleCrop == true) request.circleCrop()
    if (glideCrossFade == true) request.transition(DrawableTransitionOptions.withCrossFade())
    request.into(view)
}
