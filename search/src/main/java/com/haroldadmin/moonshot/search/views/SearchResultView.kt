package com.haroldadmin.moonshot.search.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.haroldadmin.moonshot.search.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class SearchResultView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val resultType: AppCompatTextView
    private val title: AppCompatTextView

    init {
        inflate(context, R.layout.view_search_result, this)
        resultType = findViewById(R.id.resultType)
        title = findViewById(R.id.result)
    }

    @TextProp
    fun setResultType(type: CharSequence) {
        resultType.text = type
    }

    @TextProp
    fun setResult(result: CharSequence) {
        title.text = result
    }

    @CallbackProp
    fun setOnClick(onClick: OnClickListener?) {
        onClick?.let {
            this.setOnClickListener(onClick)
        }
    }
}