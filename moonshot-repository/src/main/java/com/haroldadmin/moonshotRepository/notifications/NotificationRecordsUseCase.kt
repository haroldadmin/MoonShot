package com.haroldadmin.moonshotRepository.notifications

import com.haroldadmin.moonshot.database.NotificationRecordDao
import com.haroldadmin.moonshot.models.NotificationRecord
import com.haroldadmin.moonshot.models.NotificationType
import com.haroldadmin.moonshot.models.NotificationType.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class NotificationRecordsUseCase @Inject constructor(
    private val dao: NotificationRecordDao
) {

    suspend fun getLastNotification(): NotificationRecord? = withContext(Dispatchers.IO) {
        dao.getLastNotificationRecord()
    }

    suspend fun hasNotifiedForLaunch(
        launchFlightNumber: Int,
        notificationType: NotificationType
    ): Boolean = withContext(Dispatchers.IO) {
        val lastRecord = dao.getLastNotificationRecordForLaunch(launchFlightNumber)
        lastRecord != null && lastRecord.notificationType != notificationType
    }

    suspend fun recordNotification(
        launchFlightNumber: Int,
        notificationDate: Date,
        notificationType: NotificationType
    ) = withContext(Dispatchers.IO) {
        val notificationRecord = NotificationRecord(launchFlightNumber, notificationDate, notificationType)
        dao.save(notificationRecord)
    }
}