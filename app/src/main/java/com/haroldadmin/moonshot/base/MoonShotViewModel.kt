package com.haroldadmin.moonshot.base

import com.airbnb.mvrx.BaseMvRxViewModel
import com.haroldadmin.moonshot.BuildConfig
import com.haroldadmin.moonshot.core.Resource

abstract class MoonShotViewModel<S : MoonShotState>(initialState: S) :
    BaseMvRxViewModel<S>(initialState, BuildConfig.DEBUG) {

    protected suspend fun <T> executeAsResource(stateReducer: S.(Resource<T>) -> S, fx: suspend () -> Resource<T>) {
        setState { stateReducer(Resource.Loading) }
        val result = fx()
        setState { stateReducer(result) }
    }
}