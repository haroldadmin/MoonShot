package com.haroldadmin.moonshot

import androidx.core.content.edit
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haroldadmin.moonshot.core.AppDispatchers
import com.haroldadmin.moonshot.database.test.FakeLaunchDao
import com.haroldadmin.moonshot.database.test.FakeNotificationRecordsDao
import com.haroldadmin.moonshot.models.DatePrecision
import com.haroldadmin.moonshot.models.NotificationType
import com.haroldadmin.moonshot.models.SampleDbData
import com.haroldadmin.moonshot.notifications.NotificationConstants
import com.haroldadmin.moonshot.notifications.Notifier
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import com.haroldadmin.moonshotRepository.launch.PersistLaunchesUseCase
import com.haroldadmin.moonshotRepository.notifications.NotificationRecordsUseCase
import com.haroldadmin.spacex_api_wrapper.SampleApiData
import com.haroldadmin.spacex_api_wrapper.test.FakeLaunchesService
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
internal class NotifierTest {

    @Inject lateinit var fakeSharedPreferences: FakeSharedPreferences
    @Inject lateinit var fakeLaunchesService: FakeLaunchesService
    @Inject lateinit var fakeLaunchesDao: FakeLaunchDao
    @Inject lateinit var fakeNotificationRecordsDao: FakeNotificationRecordsDao
    @Inject lateinit var fakeNotificationManager: FakeNotificationManager
    @Inject lateinit var testDispatchers: AppDispatchers

    private lateinit var notifier: Notifier

    @Before
    fun setup() {
        DaggerTestComponent.create().inject(this)
        notifier = Notifier(
            context = ApplicationProvider.getApplicationContext(),
            nextLaunchUseCase = GetNextLaunchUseCase(
                fakeLaunchesDao,
                fakeLaunchesService,
                PersistLaunchesUseCase(fakeLaunchesDao, testDispatchers),
                testDispatchers
            ),
            notifRecordsUseCase = NotificationRecordsUseCase(fakeNotificationRecordsDao, testDispatchers),
            settings = fakeSharedPreferences,
            notificationManager = fakeNotificationManager,
            appDispatchers = testDispatchers
        )
    }

    @Test
    fun shouldNotPostNotificationsForLaunchesWithInaccurateDates() = runBlocking {
        fakeLaunchesService.seedWith(SampleApiData.Launches.one().copy(
            tentativeMaxPrecision = DatePrecision.year.name,
            upcoming = true
        ))

        fakeLaunchesDao.seedWith(SampleDbData.Launches.one().copy(
            tentativeMaxPrecision = DatePrecision.year,
            isUpcoming = true
        ))

        fakeSharedPreferences.edit {
            putBoolean(NotificationConstants.JustBeforeLaunch.settingsKey, true)
        }

        notifier.processBroadcast(notificationType = NotificationType.JustBeforeLaunch).join()

        assert(fakeNotificationManager.notifications.isEmpty()) {
            "Expected no notifications to be posted, got ${fakeNotificationManager.notifications.size}"
        }
    }

    @Test
    fun shouldPostNotificationsForLaunchesWithAccurateDates() = runBlocking {
        fakeLaunchesService.seedWith(SampleApiData.Launches.one().copy(
            tentativeMaxPrecision = DatePrecision.hour.name,
            upcoming = true
        ))

        fakeLaunchesDao.seedWith(SampleDbData.Launches.one().copy(
            tentativeMaxPrecision = DatePrecision.hour,
            isUpcoming = true
        ))

        fakeSharedPreferences.edit {
            putBoolean(NotificationConstants.JustBeforeLaunch.settingsKey, true)
        }

        notifier.processBroadcast(notificationType = NotificationType.JustBeforeLaunch).join()

        assert(fakeNotificationManager.notifications.size == 1) {
            "Expected JustBeforeLaunch notification to be posted, but got ${fakeNotificationManager.notifications}"
        }
    }

    @Test
    fun shouldNotPostNotificationsIfDisabledInSettings() = runBlocking {
        fakeLaunchesService.seedWith(SampleApiData.Launches.one().copy(
            tentativeMaxPrecision = DatePrecision.hour.name,
            upcoming = true
        ))

        fakeLaunchesDao.seedWith(SampleDbData.Launches.one().copy(
            tentativeMaxPrecision = DatePrecision.hour,
            isUpcoming = true
        ))

        fakeSharedPreferences.edit {
            putBoolean(NotificationConstants.JustBeforeLaunch.settingsKey, false)
        }

        notifier.processBroadcast(notificationType = NotificationType.JustBeforeLaunch).join()

        assert(fakeNotificationManager.notifications.size == 0) {
            "Expected no notifications to be posted, got ${fakeNotificationManager.notifications.size}"
        }
    }

    @After
    fun cleanup() {
        fakeSharedPreferences.clear()
        fakeLaunchesDao.clear()
        fakeLaunchesService.clear()
        fakeNotificationRecordsDao.clear()
        fakeNotificationManager.clear()
    }
}