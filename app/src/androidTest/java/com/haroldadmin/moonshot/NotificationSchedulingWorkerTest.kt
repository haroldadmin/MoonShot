package com.haroldadmin.moonshot

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.SampleDbData
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import com.haroldadmin.moonshot.notifications.workers.ScheduleWorker
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class NotificationSchedulingWorkerTest {

    private lateinit var context: Context
    private lateinit var workerFactory: WorkerFactory
    private lateinit var executor: Executor
    private lateinit var settings: SharedPreferences
    private lateinit var notifManager: LaunchNotificationsManager
    private lateinit var usecase: GetNextLaunchUseCase
    private lateinit var worker: ScheduleWorker

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        executor = Executors.newSingleThreadExecutor()
        usecase = mockk()
        notifManager = mockk()
        settings = mockk()
        workerFactory = object : WorkerFactory() {
            override fun createWorker(
                appContext: Context,
                workerClassName: String,
                workerParameters: WorkerParameters
            ): ListenableWorker? {
                return ScheduleWorker(appContext, workerParameters, usecase, notifManager)
            }
        }
        worker = TestListenableWorkerBuilder<ScheduleWorker>(context)
            .setWorkerFactory(workerFactory)
            .build() as ScheduleWorker
    }

    @Test
    fun fetchFailureTest() = runBlocking {
        coEvery {
            usecase.getNextLaunch()
        } returns flowOf(Resource.Error(null, null))

        coEvery {
            usecase.getNextLaunchesUntilDate(any())
        } returns flowOf(Resource.Error(null, null))

        val result = worker.doWork()
        assertTrue(result is ListenableWorker.Result.Retry)
    }

    @Test
    fun fetchSuccessfulTest() = runBlocking {
        coEvery {
            usecase.getNextLaunch()
        } returns flowOf(Resource.Success(SampleDbData.Launches.one()))
        coEvery {
            usecase.getNextLaunchesUntilDate(any())
        } returns flowOf(Resource.Success(SampleDbData.Launches.many().take(1).toList()))

        val result = worker.doWork()
        assertTrue(result is ListenableWorker.Result.Success)
    }
}
