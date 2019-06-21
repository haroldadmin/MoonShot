package com.haroldadmin.moonshot.sync

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.haroldadmin.moonshot.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class SyncManager(val context: Context) {

    companion object {
        const val KEY_BACKGROUND_SYNC = "background-sync"
    }

    private val settings = PreferenceManager.getDefaultSharedPreferences(context)

    suspend fun enableSync() = withContext(Dispatchers.Default) {
        if (settings.getBoolean(KEY_BACKGROUND_SYNC, true)) {
            log("Enabling sync")
            enqueueSyncWork()
        } else {
            log("Sync is disabled in preferences")
        }
    }

    private suspend fun enqueueSyncWork() = withContext(Dispatchers.Default) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresDeviceIdle(true)
            .setRequiresStorageNotLow(true)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<SyncWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(SyncWorker.NAME, ExistingPeriodicWorkPolicy.REPLACE, workRequest)
    }

    suspend fun disableSync() = withContext(Dispatchers.Default) {
        log("Disabling sync")

        WorkManager
            .getInstance(context)
            .cancelUniqueWork(SyncWorker.NAME)
    }
}