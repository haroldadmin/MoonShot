package com.haroldadmin.moonshot.notifications.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.haroldadmin.moonshot.di.appComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

abstract class CoroutineBroadcastReceiver : BroadcastReceiver(), CoroutineScope {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Default + exceptionHandler + Job()
    protected val broadcastReceiverComponent = appComponent().broadcastReceiversComponent().create()

    abstract suspend fun onBroadcastReceived(context: Context, intent: Intent)

    override fun onReceive(context: Context, intent: Intent) = runBlocking(coroutineContext) {
        onBroadcastReceived(context, intent)
    }
}