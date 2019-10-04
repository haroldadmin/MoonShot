package com.haroldadmin.moonshot.about.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.OnViewRecycled
import com.haroldadmin.moonshot.about.R
import com.haroldadmin.moonshot.utils.asyncText

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class LicenseView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_license, this)
    }

    private val license: AppCompatTextView = findViewById(R.id.license)

    @ModelProp
    fun setLicense(name: String) {
        license.asyncText(name)
    }

    @CallbackProp
    fun setOnClick(onClick: OnClickListener?) {
        if (onClick != null) {
            rootView.setOnClickListener(onClick)
        }
    }

    @OnViewRecycled
    fun cleanup() {
        rootView.setOnClickListener(null)
    }
}