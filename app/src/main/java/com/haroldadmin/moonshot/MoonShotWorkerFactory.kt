package com.haroldadmin.moonshot

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.haroldadmin.moonshot.sync.SyncWorker
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import org.koin.core.Koin

class MoonShotWorkerFactory(
    private val koin: Koin
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val launchesRepository = koin.get<LaunchesRepository>()
        return SyncWorker(appContext, workerParameters, launchesRepository)
    }
}