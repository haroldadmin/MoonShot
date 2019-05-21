package com.haroldadmin.moonshot.database.historical_event

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haroldadmin.moonshot.models.history.HistoricalEvent

@Dao
interface HistoricalEventsDao {

    @Query("SELECT * FROM historical_events")
    suspend fun getAllHistoricalEvents(): List<HistoricalEvent>

    @Query("SELECT * FROM historical_events WHERE id = :id")
    suspend fun getHistoricalEvent(id: Int): HistoricalEvent

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveHistoricalEvent(historicalEvent: HistoricalEvent)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveHistoricalEvents(vararg historicalEvent: HistoricalEvent)

    @Delete
    suspend fun deleteHistoricalEvent(historicalEvent: HistoricalEvent)
}