package com.haroldadmin.moonshot.features.launchDetails.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.OnViewRecycled
import com.airbnb.epoxy.TextProp
import com.google.android.material.card.MaterialCardView
import com.haroldadmin.moonshot.features.launchDetails.R
import com.haroldadmin.moonshot.utils.asyncText

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class MissionSummaryCard @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrSet, defStyleAttr) {

    init {
        inflate(context, R.layout.card_mission_summary, this)
    }

    private val cardRoot: MaterialCardView = findViewById(R.id.cardRoot)
    private val text: AppCompatTextView = findViewById(R.id.header)
    private val icon: ImageView = findViewById(R.id.icon)
    private val id: AppCompatTextView = findViewById(R.id.missionId)

    private lateinit var missionId: CharSequence
    private var onMissionClick: ((String) -> Unit)? = null

    @TextProp
    fun setMissionId(missionId: CharSequence) {
        this.missionId = missionId
    }

    @CallbackProp
    fun setOnMissionClick(onClick: ((String) -> Unit)?) {
        onClick?.let { this.onMissionClick = it }
    }

    @AfterPropsSet
    fun useProps() {
        id.asyncText { context.getString(R.string.missionIdText, missionId) }
        onMissionClick?.let { fx ->
            setOnClickListener { fx.invoke(this.missionId.toString()) }
            isClickable = true
        } ?: run {
            setOnClickListener(null)
            isClickable = false
        }
    }

    @OnViewRecycled
    fun cleanup() {
        cardRoot.apply {
            setOnClickListener(null)
            isClickable = false
        }
    }
}