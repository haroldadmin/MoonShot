package com.haroldadmin.moonshot

import androidx.core.content.edit
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
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
    @Inject lateinit var fakeNotifRecordsDao: FakeNotificationRecordsDao
    @Inject lateinit var fakeNotifManager: FakeNotificationManager

    private lateinit var notifier: Notifier

    @Before
    fun setup() {
        DaggerTestComponent.create().inject(this)
        notifier = Notifier(
            context = ApplicationProvider.getApplicationContext(),
            nextLaunchUseCase = GetNextLaunchUseCase(
                fakeLaunchesDao,
                fakeLaunchesService,
                PersistLaunchesUseCase(fakeLaunchesDao)
            ),
            notifRecordsUseCase = NotificationRecordsUseCase(fakeNotifRecordsDao),
            settings = fakeSharedPreferences,
            notificationManager = fakeNotifManager
        )
    }

    @Test
    fun shouldNotPostNotificationsForLaunchesWithInaccurateDates() {

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

        notifier.processBroadcast(notificationType = NotificationType.JustBeforeLaunch)

        assert(fakeNotifManager.notifications.isEmpty()) {
            "Expected no notifications to be posted, got ${fakeNotifManager.notifications.size}"
        }
    }

    @Test
    fun shouldPostNotificationsForLaunchesWithAccurateDates() {
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

        notifier.processBroadcast(notificationType = NotificationType.JustBeforeLaunch)

        assert(fakeNotifManager.notifications.size == 1) {
            "Expected JustBeforeLaunch notification to be posted, but got ${fakeNotifManager.notifications}"
        }
    }

    @Test
    fun shouldNotPostNotificationsIfDisabledInSettings() {
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

        notifier.processBroadcast(notificationType = NotificationType.JustBeforeLaunch)

        assert(fakeNotifManager.notifications.size == 0) {
            "Expected no notifications to be posted, got ${fakeNotifManager.notifications.size}"
        }
    }

    @After
    fun cleanup() {
        fakeSharedPreferences.clear()
        fakeLaunchesDao.clear()
        fakeLaunchesService.clear()
        fakeNotifRecordsDao.clear()
        fakeNotifManager.clear()
    }

}