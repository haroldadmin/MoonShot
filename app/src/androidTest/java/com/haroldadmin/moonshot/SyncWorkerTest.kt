package com.haroldadmin.moonshot

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.sync.SyncWorker
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class SyncWorkerTest {

    private lateinit var context: Context
    private lateinit var workerFactory: WorkerFactory
    private lateinit var executor: Executor

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        executor = Executors.newSingleThreadExecutor()
        val repo = mockk<LaunchesRepository>()
        coEvery { repo.syncLaunches() } returns Resource.Success(Unit)

        workerFactory = object : WorkerFactory() {
            override fun createWorker(
                appContext: Context,
                workerClassName: String,
                workerParameters: WorkerParameters
            ): ListenableWorker? {
                return SyncWorker(appContext, workerParameters, repo)
            }
        }
    }

    @Test
    fun testSyncWorker() = runBlocking {
        val worker = TestListenableWorkerBuilder<SyncWorker>(context)
            .setWorkerFactory(workerFactory)
            .build()
        val result = (worker as SyncWorker).doWork()
        assertTrue(result is ListenableWorker.Result.Success)
    }
}