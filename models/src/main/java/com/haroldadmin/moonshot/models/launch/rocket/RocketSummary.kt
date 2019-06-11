package com.haroldadmin.moonshot.models.launch.rocket

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.models.launch.Launch

@Entity(
    tableName = "rocket_summaries",
    indices = [Index("launch_flight_number")]
)
data class RocketSummary(
    @PrimaryKey
    @ColumnInfo(name = "launch_flight_number")
    val launchFlightNumber: Int,
    @ColumnInfo(name = "rocket_id")
    val rocketId: String,
    @ColumnInfo(name = "rocket_name")
    val rocketName: String,
    @ColumnInfo(name = "rocket_type")
    val rocketType: String,
    @Embedded
    val fairings: Fairings?
) {
    companion object {
        fun getSampleRocketSummary(launchFlightNumber: Int) =
            RocketSummary(
                launchFlightNumber,
                "falcon9",
                "Falcon 9",
                "FT",
                Fairings.getSampleFairing()
            )
    }
}