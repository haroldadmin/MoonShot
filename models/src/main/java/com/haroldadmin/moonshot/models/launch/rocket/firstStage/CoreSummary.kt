package com.haroldadmin.moonshot.models.launch.rocket.firstStage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "core_summaries",
    foreignKeys = [
        ForeignKey(
            entity = FirstStageSummary::class,
            parentColumns = ["launch_flight_number"],
            childColumns = ["launch_flight_number"]
        )
    ],
    primaryKeys = ["core_serial", "launch_flight_number"],
    indices = [Index("launch_flight_number")]
)
data class CoreSummary(
    @ColumnInfo(name = "launch_flight_number") val flightNumber: Int,
    @ColumnInfo(name = "core_serial") val serial: String,
    @ColumnInfo(name = "flight") val flight: Int?,
    @ColumnInfo(name = "block") val block: Int?,
    @ColumnInfo(name = "gridfins") val gridfins: Boolean?,
    @ColumnInfo(name = "legs") val legs: Boolean?,
    @ColumnInfo(name = "reused") val reused: Boolean?,
    @ColumnInfo(name = "land_success") val landSuccess: Boolean?,
    @ColumnInfo(name = "landing_intent") val landingIntent: Boolean?,
    @ColumnInfo(name = "landing_type") val landingType: String?,
    @ColumnInfo(name = "landing_vehicle") val landingVehicle: String?
) {

    companion object {
        fun getSampleCoreSummary(flightNumber: Int): CoreSummary =
            CoreSummary(
                flightNumber,
                "B1042",
                0,
                4,
                true,
                true,
                true,
                true,
                true,
                "",
                ""
            )
    }
}