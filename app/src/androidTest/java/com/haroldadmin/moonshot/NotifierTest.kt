package com.haroldadmin.moonshot

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haroldadmin.moonshot.database.test.FakeLaunchDao
import com.haroldadmin.moonshot.database.test.FakeNotificationRecordsDao
import com.haroldadmin.moonshot.notifications.Notifier
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import com.haroldadmin.moonshotRepository.launch.PersistLaunchesUseCase
import com.haroldadmin.moonshotRepository.notifications.NotificationRecordsUseCase
import com.haroldadmin.spacex_api_wrapper.test.FakeLaunchesService
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

    private lateinit var notifer: Notifier

    @Before
    fun setup() {
        notifer = Notifier(
            context = ApplicationProvider.getApplicationContext(),
            nextLaunchUseCase = GetNextLaunchUseCase(
                fakeLaunchesDao,
                fakeLaunchesService,
                PersistLaunchesUseCase(fakeLaunchesDao)
            ),
            notifRecordsUseCase = NotificationRecordsUseCase(fakeNotifRecordsDao),
            settings = fakeSharedPreferences
        )
    }

    @Test
    fun shouldNotPostNotificationsForLaunchesWithInaccurateDates() {
    }

}