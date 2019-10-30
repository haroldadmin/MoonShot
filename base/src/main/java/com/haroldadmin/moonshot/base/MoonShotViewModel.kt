package com.haroldadmin.moonshot.base

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import com.haroldadmin.vector.FragmentViewModelOwner
import com.haroldadmin.vector.SavedStateVectorViewModel
import com.haroldadmin.vector.VectorViewModel
import com.haroldadmin.vector.ViewModelOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class MoonShotViewModel<S : MoonShotState>(
    initState: S,
    stateStoreContext: CoroutineContext = Dispatchers.Default + Job()
) : VectorViewModel<S>(initState, stateStoreContext)

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
