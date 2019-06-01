package com.haroldadmin.moonshot.base

import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.BaseMvRxViewModel
import com.haroldadmin.moonshot.BuildConfig
import com.haroldadmin.moonshot.core.Resource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

abstract class MoonShotViewModel<S : MoonShotState>(initialState: S) :
    BaseMvRxViewModel<S>(initialState, BuildConfig.DEBUG) {

    protected suspend fun <T> executeAsResource(stateReducer: S.(Resource<T>) -> S, fx: suspend () -> Resource<T>) {
        setState { stateReducer(Resource.Loading) }
        val result = fx()
        setState { stateReducer(result) }
    }

    protected suspend fun withStateSuspend(block: suspend (state: S) -> Unit ) {
        withState { state ->
            viewModelScope.launch {
                block(state)
            }
        }
    }
}