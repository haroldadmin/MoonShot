package com.haroldadmin.moonshot.base

import android.os.Bundle
import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.haroldadmin.vector.VectorFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

abstract class MoonShotFragment : VectorFragment() {

    protected val transitionWaitTime = 500L
    private var isTransitionPostponed = false

    protected fun schedulePostponedTransition() {
        postponeEnterTransition()
        viewScope.launch {
            delay(transitionWaitTime)
            if (isTransitionPostponed && isActive) {
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

abstract class ComplexMoonShotFragment<VM : MoonShotViewModel<S>, S : MoonShotState> : MoonShotFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initDI()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        renderState(viewModel, ::renderer)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        epoxyController.cancelPendingModelBuild()
    }

    protected abstract val viewModel: VM

    protected abstract val epoxyController: EpoxyController

    protected abstract fun renderer(state: S): Unit

    protected open fun initDI() = Unit
}
