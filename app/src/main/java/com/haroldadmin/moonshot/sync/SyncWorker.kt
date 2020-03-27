package com.haroldadmin.moonshot.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.haroldadmin.moonshot.core.AppDispatchers
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.utils.log
import com.haroldadmin.moonshotRepository.SyncResourcesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext

class SyncWorker(
    appContext: Context,
    params: WorkerParameters,
    private val syncUseCase: SyncResourcesUseCase,
    private val appDispatchers: AppDispatchers
) : CoroutineWorker(appContext, params) {

    companion object { const val NAME = "sync-work" }

    @ExperimentalCoroutinesApi
    override suspend fun doWork(): Result = withContext(appDispatchers.IO) {
        log("Doing work in SyncWorker")
        when (val result = syncUseCase.sync()) {
            is Resource.Success -> Result.success()
            is Resource.Error<*, *> -> {
                log("Sync worker error. Result: $result")
                Result.retry()
            }
            else -> Result.retry()
        }
    }
}
