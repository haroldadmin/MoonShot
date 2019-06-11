package com.haroldadmin.moonshot.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager

@BindingAdapter(
    "maxLinesToggle",
    "expandedIcon",
    "collapsedIcon",
    requireAll = false
)
fun maxLinesClickListener(
    view: TextView,
    previousCollapsedMaxLines: Int,
    previousExpandedIcon: Drawable?,
    previousCollapsedIcon: Drawable?,
    newCollapsedMaxLines: Int,
    newExpandedIcon: Drawable?,
    newCollapsedIcon: Drawable?
) {
    if (previousCollapsedMaxLines != newCollapsedMaxLines) {
        // Default to the collapsed number of max lines
        view.maxLines = newCollapsedMaxLines
        // Set click listener which toggles between this and MAX_INT
        view.setOnClickListener(MaxLinesToggleClickListener(newCollapsedMaxLines, newExpandedIcon, newCollapsedIcon))
    }
}

private class MaxLinesToggleClickListener(
    private val collapsedLines: Int,
    private val expandedIcon: Drawable?,
    private val collapsedIcon: Drawable?
) : View.OnClickListener {
    private val transition = ChangeBounds().apply {
        duration = 200
        interpolator = FastOutSlowInInterpolator()
    }

    override fun onClick(view: View) {
        TransitionManager.beginDelayedTransition(findParent(view), transition)
        val textView = view as TextView
        if (textView.maxLines > collapsedLines) {
            textView.maxLines = collapsedLines
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, collapsedIcon)
        } else {
            textView.maxLines = Int.MAX_VALUE
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, expandedIcon)
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