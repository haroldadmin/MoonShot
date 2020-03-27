package com.haroldadmin.moonshot.base

import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.haroldadmin.moonshot.core.MoonShotDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

abstract class MoonShotActivity : AppCompatActivity(), CoroutineScope {
    private val appDispatchers = MoonShotDispatchers()
    override val coroutineContext: CoroutineContext = appDispatchers.Main + Job()

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }
}