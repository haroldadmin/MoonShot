package com.haroldadmin.moonshot.database.historical_event

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haroldadmin.moonshot.database.BaseDao
import com.haroldadmin.moonshot.models.history.HistoricalEvent

@Dao
abstract class HistoricalEventsDao : BaseDao<HistoricalEvent> {

    @Query("SELECT * FROM historical_events")
    abstract suspend fun getAllHistoricalEvents(): List<HistoricalEvent>

    @Query("SELECT * FROM historical_events WHERE id = :id")
    abstract suspend fun getHistoricalEvent(id: Int): HistoricalEvent
}