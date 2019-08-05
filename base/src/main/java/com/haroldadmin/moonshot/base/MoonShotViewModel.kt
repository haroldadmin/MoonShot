package com.haroldadmin.moonshot.base

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.vector.SavedStateVectorViewModel
import com.haroldadmin.vector.VectorViewModel
import com.haroldadmin.vector.loggers.androidLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class MoonShotViewModel<S : MoonShotState>(initState: S) : VectorViewModel<S>(initState) {

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
    initState: S?,
    savedStateHandle: SavedStateHandle
) : SavedStateVectorViewModel<S>(
    initialState = initState,
    stateStoreContext = Dispatchers.Default + Job(),
    logger = androidLogger(),
    savedStateHandle = savedStateHandle
)