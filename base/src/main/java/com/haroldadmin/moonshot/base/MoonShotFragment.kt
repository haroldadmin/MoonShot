package com.haroldadmin.moonshot.base

import android.util.Log
import com.haroldadmin.vector.VectorFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class MoonShotFragment : VectorFragment() {

    protected val logtag by lazy { this::class.java.simpleName }
    protected val transitionWaitTime = 500L
    private var isTransitionPostponed = false

    protected fun schedulePostponedTransition() {
        postponeEnterTransition()
        fragmentScope.launch {
            delay(transitionWaitTime)
            if (isTransitionPostponed) {
                Log.d(logtag, "Force starting postponed enter transition")
                startPostponedEnterTransition()
            }
        }
    }

    override fun postponeEnterTransition() {
        Log.d(logtag, "Postponing enter transition")
        super.postponeEnterTransition()
        isTransitionPostponed = true
    }

    override fun startPostponedEnterTransition() {
        Log.d(logtag, "Starting postponed enter transition")
        super.startPostponedEnterTransition()
        isTransitionPostponed = false
    }
}
