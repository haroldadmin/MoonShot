package com.haroldadmin.moonshot.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.haroldadmin.moonshot.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SyncManager @Inject constructor(private val context: Context) {

    companion object {
        const val KEY_BACKGROUND_SYNC = "background-sync"
    }

    suspend fun enableSync() = withContext(Dispatchers.Default) {
        log("Enqueing sync work")
        enqueueSyncWork()
    }

    private suspend fun enqueueSyncWork() = withContext(Dispatchers.Default) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<SyncWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .addTag(SyncWorker.NAME)
            .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(SyncWorker.NAME, ExistingPeriodicWorkPolicy.REPLACE, workRequest)
    }

    suspend fun disableSync() = withContext(Dispatchers.Default) {

        log("Disabling sync")

        WorkManager
            .getInstance(context)
            .cancelAllWorkByTag(SyncWorker.NAME)
    }
}