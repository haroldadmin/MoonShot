package com.haroldadmin.moonshotRepository.notifications

import com.haroldadmin.moonshot.database.NotificationRecordDao
import com.haroldadmin.moonshot.models.NotificationRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationRecordsUseCase @Inject constructor(
    private val dao: NotificationRecordDao
) {

    suspend fun getLastNotification(): NotificationRecord? = withContext(Dispatchers.IO) {
        dao.getLastNotificationRecord()
    }

    suspend fun hasNotifiedForLaunch(launchFlightNumber: Int): Boolean = withContext(Dispatchers.IO) {
        dao.hasNotifiedForLaunch(launchFlightNumber) == 1
    }
}