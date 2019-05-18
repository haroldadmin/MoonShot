package com.haroldadmin.moonshot.database.launch.rocket

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.database.launch.Launch

@Entity(
    tableName = "rocket_summaries",
    foreignKeys = [
        ForeignKey(
            entity = Launch::class,
            parentColumns = ["flight_number"],
            childColumns = ["launch_flight_number"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("launch_flight_number")]
)
data class RocketSummary(
    @PrimaryKey
    @ColumnInfo(name = "rocket_id")
    val rocketId: String,
    @ColumnInfo(name = "launch_flight_number")
    val launchFlightNumber: Int,
    @ColumnInfo(name = "rocket_name")
    val rocketName: String,
    @ColumnInfo(name = "rocket_type")
    val rocketType: String,
    @Embedded
    val fairings: Fairings
) {
    companion object {
        internal fun getSampleRocketSummary(launchFlightNumber: Int) =
                RocketSummary(
                    "falcon9",
                    launchFlightNumber,
                    "Falcon 9",
                    "FT",
                    Fairings.getSampleFairing()
                )
    }
}