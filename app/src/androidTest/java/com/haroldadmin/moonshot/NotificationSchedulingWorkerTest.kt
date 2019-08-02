package com.haroldadmin.moonshot

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import com.haroldadmin.moonshot.notifications.workers.DailyNotificationSchedulingWorker
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class NotificationSchedulingWorkerTest {

    private lateinit var context: Context
    private lateinit var workerFactory: WorkerFactory
    private lateinit var executor: Executor
    private lateinit var settings: SharedPreferences
    private lateinit var notifManager: LaunchNotificationsManager
    private lateinit var repo: LaunchesRepository

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        executor = Executors.newSingleThreadExecutor()
        repo = mockk()
        notifManager = mockk()
        settings = mockk()

        coEvery { repo.getNextLaunchFromDatabase(any()) } returns Launch.getSampleLaunch()

        workerFactory = object : WorkerFactory() {
            override fun createWorker(
                appContext: Context,
                workerClassName: String,
                workerParameters: WorkerParameters
            ): ListenableWorker? {
                return DailyNotificationSchedulingWorker(
                    appContext,
                    workerParameters,
                    repo,
                    notifManager,
                    settings
                )
            }
        }
    }

    @Test
    fun notificationsDisabledInSettingsTest() = runBlocking {
        every { settings.getBoolean(any(), any()) } returns false
        val worker = TestListenableWorkerBuilder<DailyNotificationSchedulingWorker>(context)
            .setWorkerFactory(workerFactory)
            .build() as DailyNotificationSchedulingWorker

        val result = worker.doWork()
        assertTrue(result is ListenableWorker.Result.Failure)
    }

    @Test
    fun notificationsEnabledInSettingsTest() = runBlocking {
        every { settings.getBoolean(any(), any()) } returns true
        val worker = TestListenableWorkerBuilder<DailyNotificationSchedulingWorker>(context)
            .setWorkerFactory(workerFactory)
            .build() as DailyNotificationSchedulingWorker

        val result = worker.doWork()
        assertTrue(result is ListenableWorker.Result.Success)
    }
}