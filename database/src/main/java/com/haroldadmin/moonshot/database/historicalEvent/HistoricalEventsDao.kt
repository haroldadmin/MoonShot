package com.haroldadmin.moonshot.database.historicalEvent

import androidx.room.Dao
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