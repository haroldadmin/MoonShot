package com.haroldadmin.moonshot.features.about.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.haroldadmin.moonshot.utils.asyncText
import com.haroldadmin.moonshot.features.about.AboutState
import com.haroldadmin.moonshot.features.about.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class AboutAppCard @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrSet, defStyleAttr) {

    init {
        inflate(context, R.layout.card_about_app, this)
    }

    private val appIcon: ImageView = findViewById(R.id.appIcon)
    private val appName: AppCompatTextView = findViewById(R.id.appName)
    private val versionCode: AppCompatTextView = findViewById(R.id.versionCode)
    private val versionName: AppCompatTextView = findViewById(R.id.versionName)

    private var state: AboutState? = null

    @ModelProp
    fun setState(state: AboutState) {
        this.state = state
    }

    @AfterPropsSet
    fun useProps() {
        versionCode.asyncText {
            "Version Code: ${state!!.versionCode}"
        }
        versionName.asyncText {
            "Version Name: ${state!!.versionName}"
        }
    }
}