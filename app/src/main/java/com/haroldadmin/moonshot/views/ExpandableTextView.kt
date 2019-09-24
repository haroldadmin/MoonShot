package com.haroldadmin.moonshot.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.base.gone
import com.haroldadmin.moonshot.base.show
import com.haroldadmin.moonshot.utils.asyncText

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ExpandableTextView @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null,
    defStyleAttr: Int = 0
): FrameLayout(context, attrSet, defStyleAttr) {

    init {
        inflate(context, R.layout.view_expandable_text, this)
    }

    private val headerText: AppCompatTextView = findViewById(R.id.headingText)
    private val contentText: AppCompatTextView = findViewById(R.id.contentText)
    private val toggleIcon: ImageView = findViewById(R.id.toggleIcon)
    private val collapsedIcon = context.getDrawable(R.drawable.ic_round_expand_more_24px)
    private val expandedIcon = context.getDrawable(R.drawable.ic_round_expand_less_24px)

    private val shrunkLines = context.resources.getInteger(R.integer.maxLinesCollapsed)
    private val transitionDuration = context.resources.getInteger(R.integer.expandableTextToggleDuration)
    private var header: CharSequence = ""
    private var content: CharSequence = ""

    private var isExpanded: Boolean = false
    private val shouldAllowToggle: Boolean
        get() {
            return true
//            return content.isNotEmpty() && contentText.lineCount > shrunkLines
        }

    private val transition: Transition = ChangeBounds().apply {
        duration = transitionDuration.toLong()
        interpolator = FastOutSlowInInterpolator()
    }

    private val toggleClickListener = OnClickListener {
        if (!shouldAllowToggle) { return@OnClickListener }

        TransitionManager.beginDelayedTransition(findParent(this), transition)

        if (isExpanded) {
            contentText.maxLines = shrunkLines
            toggleIcon.setImageDrawable(collapsedIcon)

            isExpanded = false
        } else {
            contentText.maxLines = Integer.MAX_VALUE
            toggleIcon.setImageDrawable(expandedIcon)

            isExpanded = true
        }
    }

    @TextProp
    fun setHeader(text: CharSequence) {
        header = text
    }

    @TextProp
    fun setContent(text: CharSequence) {
        content = text
    }

    @AfterPropsSet
    fun useProps() {

        headerText.asyncText(header)
        contentText.asyncText(content)
        contentText.maxLines = shrunkLines

        if (!shouldAllowToggle) {
            toggleIcon.gone()
        } else {
            toggleIcon.apply {
                show()
                setImageDrawable(collapsedIcon)
            }
            rootView.setOnClickListener(toggleClickListener)
        }
    }

    private fun findParent(view: View): ViewGroup {
        var parentView: View? = view
        while (parentView != null) {
            val parent = parentView.parent as View?
            if (parent is RecyclerView) {
                return parent
            }
            parentView = parent
        }
        // If we reached here we didn't find a RecyclerView in the parent tree, so lets just use our parent
        return view.parent as ViewGroup
    }
}