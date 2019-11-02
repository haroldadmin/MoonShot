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
import com.google.android.material.card.MaterialCardView
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.models.LinkPreview
import com.haroldadmin.moonshot.utils.asyncText

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class LinkPreviewCard @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrSet, defStyleAttr) {

    init {
        inflate(context, R.layout.card_link_preview, this)
    }

    private val cardRoot: MaterialCardView = findViewById(R.id.cardRoot)
    private val image: ImageView = findViewById(R.id.image)
    private val title: AppCompatTextView = findViewById(R.id.title)
    private val description: AppCompatTextView = findViewById(R.id.description)

    private lateinit var linkPreview: LinkPreview
    private var onLinkClick: ((LinkPreview) -> Unit)? = null

    @ModelProp
    fun setLinkPreview(linkPreview: LinkPreview) {
        this.linkPreview = linkPreview
    }

    @CallbackProp
    fun setOnLinkClick(onClick: ((LinkPreview) -> Unit)?) {
        onClick?.let { this.onLinkClick = onClick }
    }

    @AfterPropsSet
    fun useProps() {
        image.load(linkPreview.image)
        title.asyncText { linkPreview.title ?: linkPreview.website }
        description.asyncText { linkPreview.description ?: "" }
        onLinkClick?.let { onClick ->
            cardRoot.apply {
                setOnClickListener { onClick(linkPreview) }
                isClickable = true
            }
        } ?: cardRoot.apply {
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