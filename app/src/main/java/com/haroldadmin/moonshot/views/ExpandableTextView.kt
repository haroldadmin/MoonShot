package com.haroldadmin.moonshot.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
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

/**
 * A compound view which supports expanding/collapsing a text view containing long text.
 *
 * When the content text is supplied to this class, it checks if it's length is greater than
 * a predefined character limit. If it is, then it sets the maxLines attribute on the textView to a predefined
 * small integer constant.
 *
 * We don't use TextView's lineCount attribute to decide if the text is too long, because it sometimes returns 0
 * just after the text is set. Also, by relying on a predefined max character count value we can also use
 * Precomputed Text.
 */
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

    private val maxChars = context.resources.getInteger(R.integer.maxCollapsedChars)
    private val shrunkLines = context.resources.getInteger(R.integer.maxLinesCollapsed)
    private val transitionDuration = context.resources.getInteger(R.integer.expandableTextToggleDuration)
    private var header: CharSequence = ""
    private var content: CharSequence = ""

    private val shouldAllowToggle: Boolean
        get() { return content.length > maxChars }

    private var isExpanded: Boolean = false

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

        if (content.length > maxChars) {
            contentText.maxLines = shrunkLines
            toggleIcon.apply {
                show()
                setImageDrawable(collapsedIcon)
            }
            isExpanded = false
            rootView.setOnClickListener(toggleClickListener)
        } else {
            isExpanded = true
            toggleIcon.apply {
                gone()
            }
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