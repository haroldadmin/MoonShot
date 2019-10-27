package com.haroldadmin.moonshot.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshotRepository.SyncResourcesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext

class SyncWorker(
    appContext: Context,
    params: WorkerParameters,
    private val syncUseCase: SyncResourcesUseCase
) : CoroutineWorker(appContext, params) {

    companion object { const val NAME = "sync-work" }

    @ExperimentalCoroutinesApi
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        when (syncUseCase.sync()) {
            is Resource.Success -> Result.success()
            else -> Result.retry()
        }
    }
}
