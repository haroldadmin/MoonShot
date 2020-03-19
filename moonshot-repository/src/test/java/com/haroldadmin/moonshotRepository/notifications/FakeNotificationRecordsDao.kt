package com.haroldadmin.moonshotRepository.notifications

import com.haroldadmin.moonshot.database.NotificationRecordDao
import com.haroldadmin.moonshot.models.NotificationRecord
import com.haroldadmin.moonshotRepository.FakeStatefulDao

internal class FakeNotificationRecordsDao : NotificationRecordDao(), FakeStatefulDao<NotificationRecord> {

    override val items: MutableList<NotificationRecord> = mutableListOf()

    override suspend fun getLastNotificationRecord(): NotificationRecord? {
        return items.lastOrNull()
    }

    override suspend fun hasNotifiedForLaunch(launchFlightNumber: Int): Int {
        val record = items.find { it.launchFlightNumber == launchFlightNumber }
        return if (record != null) {
            1
        } else {
            0
        }
    }

    override suspend fun getLastNotificationRecordForLaunch(flightNumber: Int): NotificationRecord? {
        return items.last { it.launchFlightNumber == flightNumber }
    }
}