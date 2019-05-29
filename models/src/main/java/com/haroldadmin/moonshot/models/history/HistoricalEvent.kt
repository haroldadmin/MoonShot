package com.haroldadmin.moonshot.models.history

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "historical_events")
data class HistoricalEvent(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "event_data_utc")
    val date: Date,
    @ColumnInfo(name = "flight_number")
    val flightNumber: Int,
    @ColumnInfo(name = "details")
    val details: String,
    @Embedded
    val links: Links
) {

    companion object {
        fun getSampleHistoricalEvent(): HistoricalEvent {
            return HistoricalEvent(
                id = 1,
                title = "Falcon 1 Makes History",
                date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2008-09-28T23:15:00Z"),
                flightNumber = 4,
                details = "Falcon 1 becomes the first privately developed liquid fuel rocket to reach Earth orbit.",
                links = Links.getSampleLinks()
            )
        }
    }
}
