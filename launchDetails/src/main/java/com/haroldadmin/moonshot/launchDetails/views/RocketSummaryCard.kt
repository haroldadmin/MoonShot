package com.haroldadmin.moonshot.launchDetails.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.OnViewRecycled
import com.airbnb.epoxy.TextProp
import com.google.android.material.card.MaterialCardView
import com.haroldadmin.moonshot.R as appR
import com.haroldadmin.moonshot.launchDetails.R
import com.haroldadmin.moonshot.models.launch.RocketSummaryMinimal
import com.haroldadmin.moonshot.utils.asyncText

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class RocketSummaryCard @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrSet, defStyleAttr) {

    init {
        inflate(context, R.layout.card_rocket_summary, this)
    }

    private val card: MaterialCardView = findViewById(R.id.cardRoot)
    private val header: AppCompatTextView = findViewById(R.id.headerText)
    private val name: AppCompatTextView = findViewById(R.id.rocketName)
    private val typeLabel: AppCompatTextView = findViewById(R.id.rocketTypeLabel)
    private val type: AppCompatTextView = findViewById(R.id.rocketType)

    private lateinit var rocket: RocketSummaryMinimal
    private var onClick: OnClickListener? = null

    @TextProp
    fun setHeader(text: CharSequence?) {
        header.asyncText { text ?: context.getString(appR.string.launchRocketHeaderText) }
    }

    @TextProp
    fun setTypeHeader(text: CharSequence?) {
        typeLabel.asyncText { text ?: context.getString(appR.string.launchRocketTypeLabel) }
    }

    @ModelProp
    fun setRocket(rocket: RocketSummaryMinimal) {
        this.rocket = rocket
    }

    @CallbackProp
    fun setOnRocketClick(onClick: OnClickListener?) {
        this.onClick = onClick
    }

    @AfterPropsSet
    fun useProps() {
        name.asyncText { rocket.rocketName }
        type.asyncText { rocket.rocketType }
        onClick?.let { clickListener ->
            card.apply {
                setOnClickListener(clickListener)
                isClickable = true
            }
        } ?: run {
            card.apply {
                isClickable = false
            }
        }
    }

    @OnViewRecycled
    fun cleanup() {
        card.apply {
            setOnClickListener(null)
            isClickable = false
        }
        onClick = null
    }
}