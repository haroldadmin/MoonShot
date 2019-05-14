package com.haroldadmin.moonshot.database.launch

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "telemetry")
data class Telemetry(
    @ColumnInfo(name = "flight_club")
    val flightClub: String
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "telemetry_id")
    var id: Int? = null

}