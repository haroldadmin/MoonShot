package com.haroldadmin.moonshot.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade

@BindingAdapter(
    "glideImage",
    "glideImageError",
    "glideImageFallback",
    "glideCircleCrop",
    requireAll = false
)
fun loadImage(
    view: ImageView,
    glideImage: String?,
    glideImageError: Drawable?,
    glideImageFallback: Drawable?,
    glideCircleCrop: Boolean?
) {
    val request = GlideApp.with(view)
        .load(glideImage)
        .transition(withCrossFade())
        .error(glideImageError)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .fallback(glideImageFallback)

    if (glideCircleCrop == true) request.circleCrop()
    request.into(view)
}
