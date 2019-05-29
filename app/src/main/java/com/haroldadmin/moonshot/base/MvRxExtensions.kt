package com.haroldadmin.moonshot.base

import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.withState

/**
 * For use with [MoonShotFragment.epoxyController].
 *
 * This builds Epoxy models in a background thread.
 */
open class MoonShotEpoxyController(
    val buildModelsCallback: EpoxyController.() -> Unit = {}
) : AsyncEpoxyController() {

    override fun buildModels() {
        buildModelsCallback()
    }
}

/**
 * Create a [MoonShotEpoxyController] that builds models with the given callback.
 */
fun MoonShotFragment.simpleController(
    buildModels: EpoxyController.() -> Unit
) = MoonShotEpoxyController {
    // Models are built asynchronously, so it is possible that this is called after the fragment
    // is detached under certain race conditions.
    if (view == null || isRemoving) return@MoonShotEpoxyController
    buildModels()
}

/**
 * Create a [MoonShotEpoxyController] that builds models with the given callback.
 * When models are built the current state of the viewmodel will be provided.
 */
fun <S : MvRxState, A : MoonShotViewModel<S>> MoonShotFragment.simpleController(
    viewModel: A,
    buildModels: EpoxyController.(state: S) -> Unit
) = MoonShotEpoxyController {
    if (view == null || isRemoving) return@MoonShotEpoxyController
    withState(viewModel) { state ->
        buildModels(state)
    }
}

/**
 * Create a [MoonShotEpoxyController] that builds models with the given callback.
 * When models are built the current state of the viewmodels will be provided.
 */
fun <A : MoonShotViewModel<B>, B : MvRxState, C : MoonShotViewModel<D>, D : MvRxState> MoonShotFragment.simpleController(
    viewModel1: A,
    viewModel2: C,
    buildModels: EpoxyController.(state1: B, state2: D) -> Unit
) = MoonShotEpoxyController {
    if (view == null || isRemoving) return@MoonShotEpoxyController
    withState(viewModel1, viewModel2) { state1, state2 ->
        buildModels(state1, state2)
    }
}

/**
 * Create a [MoonShotEpoxyController] that builds models with the given callback.
 * When models are built the current state of the viewmodels will be provided.
 */
fun <A : MoonShotViewModel<B>, B : MvRxState, C : MoonShotViewModel<D>, D : MvRxState, E : MoonShotViewModel<F>, F : MvRxState> MoonShotFragment.simpleController(
    viewModel1: A,
    viewModel2: C,
    viewModel3: E,
    buildModels: EpoxyController.(state1: B, state2: D, state3: F) -> Unit
) = MoonShotEpoxyController {
    if (view == null || isRemoving) return@MoonShotEpoxyController
    withState(viewModel1, viewModel2, viewModel3) { state1, state2, state3 ->
        buildModels(state1, state2, state3)
    }
}
