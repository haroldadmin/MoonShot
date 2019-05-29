package com.haroldadmin.moonshot.base

import com.airbnb.mvrx.BaseMvRxFragment

abstract class MoonShotFragment: BaseMvRxFragment() {

    abstract fun epoxyController(): MoonShotEpoxyController

}