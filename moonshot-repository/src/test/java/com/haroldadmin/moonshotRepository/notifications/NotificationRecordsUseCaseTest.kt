package com.haroldadmin.moonshotRepository.notifications

import com.haroldadmin.moonshot.database.NotificationRecordDao
import com.haroldadmin.moonshot.models.NotificationRecord
import com.haroldadmin.moonshot.models.NotificationType
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.Date

internal class NotificationRecordsUseCaseTest {

    private val dao: NotificationRecordDao = FakeNotificationRecordsDao()
    private val usecase = NotificationRecordsUseCase(dao)

    init {
        runBlocking {
            dao.save(NotificationRecord(1, Date(), NotificationType.JustBeforeLaunch))
            dao.save(NotificationRecord(2, Date(), NotificationType.JustBeforeLaunch))
        }
    }

    @Test
    fun `should return last notification record correctly when requesting the last notification`() = runBlocking {
        val record = usecase.getLastNotification()
        assert(record != null) {
            "Expected a notification record, got null"
        }
        assert(record!!.launchFlightNumber == 2) {
            "Expected last notification flight number to be 2, got ${record.launchFlightNumber}"
        }
    }

    @Test
    fun `should return whether a notification has been posted or not correctly`() = runBlocking {
        val hasNotified = usecase.hasNotifiedForLaunch(1)
        assert(hasNotified) {
            "Expected notification to have been posted, but it wasn't"
        }
    }
}