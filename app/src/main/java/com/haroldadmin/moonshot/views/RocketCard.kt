package com.haroldadmin.moonshot.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import coil.api.load
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.OnViewRecycled
import com.airbnb.epoxy.TextProp
import com.google.android.material.card.MaterialCardView
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.models.rocket.RocketMinimal
import com.haroldadmin.moonshot.utils.asyncText

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class RocketCard @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrSet, defStyleAttr) {

    init {
        inflate(context, R.layout.card_rocket, this)
    }

    private val card: MaterialCardView = findViewById(R.id.cardRoot)
    private val header: AppCompatTextView = findViewById(R.id.headerText)
    private val name: AppCompatTextView = findViewById(R.id.rocketName)
    private val icon: ImageView = findViewById(R.id.rocketIcon)
    private val status: AppCompatTextView = findViewById(R.id.rocketStatus)

    private lateinit var rocket: RocketMinimal

    private var onClick: OnClickListener? = null

    @TextProp
    fun setHeader(text: CharSequence?) {
        header.asyncText { text ?: context.getString(R.string.itemRocketHeader) }
    }

    @ModelProp
    fun setRocket(rocket: RocketMinimal) {
        this.rocket = rocket
    }

    @CallbackProp
    fun setOnRocketClick(onClick: OnClickListener?) {
        this.onClick = onClick
    }

    @AfterPropsSet
    fun useProps() {
        name.asyncText { rocket.rocketName }
        icon.load(R.drawable.ic_round_rocket_small)
        status.asyncText(rocket.statusText)
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