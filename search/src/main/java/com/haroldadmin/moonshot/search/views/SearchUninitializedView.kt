package com.haroldadmin.moonshot.search.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.RawRes
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.lottie.LottieAnimationView
import com.haroldadmin.moonshot.search.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class SearchUninitializedView @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null,
    defStyleAttr: Int = 0
): FrameLayout(context, attrSet, defStyleAttr) {

    init {
        inflate(context, R.layout.view_search_uninitialized, this)
    }

    private val lottieView: LottieAnimationView = findViewById(R.id.animation)
    @RawRes private var animationRes: Int? = null

    @ModelProp
    fun setAnimation(@RawRes animationRes: Int?) {
        this.animationRes = animationRes
    }

    @AfterPropsSet
    fun useProps() {
        animationRes?.let {
            lottieView.setAnimation(it)
        }
    }
}
