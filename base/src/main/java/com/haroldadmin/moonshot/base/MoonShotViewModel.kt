package com.haroldadmin.moonshot.base

import android.util.Log
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.vector.viewModel.VectorViewModel

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
            Log.e(javaClass.simpleName, e.localizedMessage)
            Resource.Error(data = null, error = e)
        }
        setState { reducer(this, result) }

        after()
    }
}
