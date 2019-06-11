package com.haroldadmin.moonshot.base

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
                startPostponedEnterTransition()
            }
        }
    }

    override fun postponeEnterTransition() {
        super.postponeEnterTransition()
        isTransitionPostponed = true
    }

    override fun startPostponedEnterTransition() {
        super.startPostponedEnterTransition()
        isTransitionPostponed = false
    }
}
