package com.haroldadmin.moonshot.base

import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

abstract class MoonShotActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.Main + Job()

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }
}