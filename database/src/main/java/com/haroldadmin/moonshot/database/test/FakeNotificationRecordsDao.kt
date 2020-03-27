package com.haroldadmin.moonshot.database.test

import com.haroldadmin.moonshot.database.NotificationRecordDao
import com.haroldadmin.moonshot.models.NotificationRecord

class FakeNotificationRecordsDao: NotificationRecordDao() {

    private val notifRecords = mutableListOf<NotificationRecord>()

    fun seedWith(vararg record: NotificationRecord) {
        notifRecords.addAll(record)
    }

    override suspend fun getLastNotificationRecord(): NotificationRecord? {
        return notifRecords.lastOrNull()
    }

    override suspend fun hasNotifiedForLaunch(launchFlightNumber: Int): Int {
        val hasNotified = notifRecords.find { it.launchFlightNumber == launchFlightNumber } != null
        return if (hasNotified) 1  else 0
    }

    override suspend fun getLastNotificationRecordForLaunch(flightNumber: Int): NotificationRecord? {
        return notifRecords.last { it.launchFlightNumber == flightNumber }
    }

    override suspend fun save(obj: NotificationRecord) {
        notifRecords.add(obj)
    }

    override suspend fun saveAll(objs: List<NotificationRecord>) {
        notifRecords.addAll(objs)
    }

    override suspend fun update(obj: NotificationRecord) {
        val index = notifRecords.indexOf(obj)
        notifRecords[index] = obj
    }

    override suspend fun updateAll(objs: List<NotificationRecord>) {
        objs.forEach { obj -> update(obj) }
    }

    override suspend fun delete(obj: NotificationRecord) {
        notifRecords.remove(obj)
    }

    override suspend fun deleteAll(objs: List<NotificationRecord>) {
        notifRecords.removeAll(objs)
    }

}