package com.haroldadmin.moonshotRepository.notifications

import com.haroldadmin.moonshot.core.AppDispatchers
import com.haroldadmin.moonshot.database.NotificationRecordDao
import com.haroldadmin.moonshot.models.NotificationRecord
import com.haroldadmin.moonshot.models.NotificationType
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class NotificationRecordsUseCase @Inject constructor(
    private val dao: NotificationRecordDao,
    private val appDispatchers: AppDispatchers
) {

    suspend fun getLastNotification(): NotificationRecord? = withContext(appDispatchers.IO) {
        dao.getLastNotificationRecord()
    }

    suspend fun hasNotifiedForLaunch(
        launchFlightNumber: Int,
        notificationType: NotificationType
    ): Boolean = withContext(appDispatchers.IO) {
        val lastRecord = dao.getLastNotificationRecordForLaunch(launchFlightNumber)
        lastRecord != null && lastRecord.notificationType != notificationType
    }

    suspend fun recordNotification(
        launchFlightNumber: Int,
        notificationDate: Date,
        notificationType: NotificationType
    ) = withContext(appDispatchers.IO) {
        val notificationRecord = NotificationRecord(launchFlightNumber, notificationDate, notificationType)
        dao.save(notificationRecord)
    }
}