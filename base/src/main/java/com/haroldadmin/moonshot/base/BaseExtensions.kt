package com.haroldadmin.moonshot.base

import android.app.Notification
import android.content.Context
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.annotation.AnimRes
import androidx.core.app.NotificationManagerCompat
import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.CarouselModelBuilder
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.TypedEpoxyController
import com.haroldadmin.vector.withState

open class MoonShotEpoxyController(
    val buildModelsCallback: EpoxyController.() -> Unit = {}
) : AsyncEpoxyController() {

    override fun buildModels() {
        buildModelsCallback()
    }
}

open class MoonShotTypedEpoxyController<S : MoonShotState>(
    val buildModelsCallback: EpoxyController.(state: S) -> Unit = {}
) : TypedEpoxyController<S>() {
    override fun buildModels(data: S) {
        buildModelsCallback(data)
    }
}

open class MoonShotAsyncTypedEpoxyController<S : MoonShotState>(
    val buildModelsCallback: EpoxyController.(state: S) -> Unit = {}
) : TypedEpoxyController<S>() {

    override fun buildModels(data: S) {
        buildModelsCallback(data)
    }
}

fun <S : MoonShotState> MoonShotFragment.typedEpoxyController(
    viewModel: MoonShotViewModel<S>,
    buildModels: EpoxyController.(S) -> Unit
) = MoonShotTypedEpoxyController<S> {
    if (view == null || isRemoving) return@MoonShotTypedEpoxyController
    withState(viewModel) { state ->
        buildModels(state)
    }
}

fun MoonShotFragment.simpleController(
    buildModels: EpoxyController.() -> Unit
) = MoonShotEpoxyController {
    // Models are built asynchronously, so it is possible that this is called after the fragment
    // is detached under certain race conditions.
    if (view == null || isRemoving) return@MoonShotEpoxyController
    buildModels()
}

fun <S : MoonShotState> MoonShotFragment.asyncController(
    viewModel: MoonShotViewModel<S>,
    buildModels: EpoxyController.(state: S) -> Unit
) = MoonShotAsyncTypedEpoxyController<S>() {
    if (view == null || isRemoving) return@MoonShotAsyncTypedEpoxyController
    withState(viewModel) { state ->
        buildModels(state)
    }
}

inline fun EpoxyController.carousel(modelInitializer: CarouselModelBuilder.() -> Unit) {
    CarouselModel_().apply {
        modelInitializer()
    }.addTo(this)
}

inline fun <T> CarouselModelBuilder.withModelsFrom(
    items: List<T>,
    modelBuilder: (T) -> EpoxyModel<*>
) {
    models(items.map { modelBuilder(it) })
}

inline fun <T, R> CarouselModelBuilder.withModelsFrom(
    items: Map<T, R>,
    modelBuilder: (T, R) -> EpoxyModel<*>
) {
    models(items.map { (key, value) -> modelBuilder(key, value) })
}

fun Notification.notify(context: Context, code: Int) {
    NotificationManagerCompat.from(context)
        .notify(code, this)
}

fun MoonShotFragment.layoutAnimation(@AnimRes id: Int): LayoutAnimationController {
    return AnimationUtils.loadLayoutAnimation(requireContext(), id)
}