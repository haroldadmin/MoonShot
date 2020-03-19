package com.haroldadmin.moonshot.database

import androidx.room.Dao
import androidx.room.Query
import com.haroldadmin.moonshot.models.NotificationRecord

@Dao
abstract class NotificationRecordDao : BaseDao<NotificationRecord> {

    @Query("""
        SELECT * FROM notification_records
        ORDER BY notification_date DESC
        LIMIT 1
    """)
    abstract suspend fun getLastNotificationRecord(): NotificationRecord?

    @Query("""
        SELECT EXISTS(
        SELECT 1 FROM notification_records
        WHERE launch_flight_number = :launchFlightNumber
        )
    """)
    abstract suspend fun hasNotifiedForLaunch(launchFlightNumber: Int): Int

    @Query("""
        SELECT * FROM notification_records
        WHERE launch_flight_number = :flightNumber
        ORDER BY notification_date DESC
        LIMIT 1
    """)
    abstract suspend fun getLastNotificationRecordForLaunch(flightNumber: Int): NotificationRecord?
}