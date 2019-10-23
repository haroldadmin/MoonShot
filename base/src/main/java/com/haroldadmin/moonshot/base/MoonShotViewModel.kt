package com.haroldadmin.moonshot.base

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.vector.ActivityViewModelOwner
import com.haroldadmin.vector.FragmentViewModelOwner
import com.haroldadmin.vector.SavedStateVectorViewModel
import com.haroldadmin.vector.VectorViewModel
import com.haroldadmin.vector.ViewModelOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.android.getKoin
import org.koin.core.Koin
import kotlin.coroutines.CoroutineContext

abstract class MoonShotViewModel<S : MoonShotState>(
    initState: S,
    stateStoreContext: CoroutineContext = Dispatchers.Default + Job()
) : VectorViewModel<S>(initState, stateStoreContext) {

    suspend fun <T : Any> executeAsResource(
        reducer: S.(Resource<T>) -> S,
        before: suspend () -> Unit = {},
        after: suspend () -> Unit = {},
        block: suspend () -> Resource<T>
    ) {

        before()

        setState { reducer(this, Resource.Loading) }
        val result = try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(data = null, error = e)
        }
        setState { reducer(this, result) }

        after()
    }
}

abstract class SavedStateMoonShotViewModel<S : MoonShotState>(
    initState: S,
    savedStateHandle: SavedStateHandle
) : SavedStateVectorViewModel<S>(
    initialState = initState,
    stateStoreContext = Dispatchers.Default + Job(),
    savedStateHandle = savedStateHandle
)

// TODO Restrict T to T: NavArgs
inline fun <reified T> ViewModelOwner.safeArgs(): T {
    this as FragmentViewModelOwner
    val argsBundle = args() ?: throw IllegalStateException("Fragment arguments for $fragment not set")
    val method = T::class.java.getMethod("fromBundle", Bundle::class.java)
    return method.invoke(null, argsBundle) as T
}

fun ViewModelOwner.koin(): Koin = when (this) {
        is FragmentViewModelOwner -> fragment.getKoin()
        is ActivityViewModelOwner -> activity.getKoin()
}