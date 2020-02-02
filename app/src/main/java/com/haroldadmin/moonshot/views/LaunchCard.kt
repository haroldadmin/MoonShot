package com.haroldadmin.moonshot.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import coil.api.load
import coil.transform.CircleCropTransformation
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.OnViewRecycled
import com.airbnb.epoxy.TextProp
import com.google.android.material.card.MaterialCardView
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.missionPatch
import com.haroldadmin.moonshot.utils.asyncText
import com.haroldadmin.moonshot.utils.formatDate

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class LaunchCard @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defaultStyleAttr) {

    init {
        inflate(context, R.layout.card_launch, this)
    }

    private val card: MaterialCardView = findViewById(R.id.cardRoot)
    private val header: AppCompatTextView = findViewById(R.id.headerText)
    private val name: AppCompatTextView = findViewById(R.id.launchName)
    private val missionPatch: ImageView = findViewById(R.id.missionPatch)
    private val date: AppCompatTextView = findViewById(R.id.launchDate)
    private val site: AppCompatTextView = findViewById(R.id.launchSite)

    private var onLaunchClick: OnClickListener? = null
    private lateinit var launch: Launch

    @TextProp
    fun setHeader(text: CharSequence?) = header.asyncText { text ?: context.getString(R.string.launchCardHeaderText) }

    @ModelProp
    fun setLaunch(launch: Launch) {
        this.launch = launch
    }

    @CallbackProp
    fun setOnLaunchClick(onLaunchClick: OnClickListener?) {
        this.onLaunchClick = onLaunchClick
    }

    @AfterPropsSet
    fun useProps() {
        name.asyncText(launch.missionName)

        missionPatch.load(launch.missionPatch()) {
            crossfade(true)
            error(R.drawable.ic_rocket)
            placeholder(R.drawable.ic_rocket)
            transformations(CircleCropTransformation())
        }

        date.asyncText {
            context.formatDate(launch.launchDateUtc, launch.tentativeMaxPrecision.dateFormat)
        }

        site.asyncText { launch.launchSite?.siteName ?: context.getString(R.string.launchCardNoSiteNameText) }

        onLaunchClick?.let {
            card.apply {
                setOnClickListener(it)
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
        onLaunchClick = null
    }
}
