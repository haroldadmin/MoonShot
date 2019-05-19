package com.haroldadmin.moonshot.base

import com.airbnb.mvrx.BaseMvRxViewModel
import com.haroldadmin.moonshot.BuildConfig

abstract class MoonShotViewModel<S : MoonShotState>(initialState: S) :
    BaseMvRxViewModel<S>(initialState, BuildConfig.DEBUG)