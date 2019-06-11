package com.haroldadmin.moonshot.models.launch.rocket.firstStage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary

@Entity(
    tableName = "first_stage_summaries"
)
data class FirstStageSummary(
    @PrimaryKey
    @ColumnInfo(name = "launch_flight_number")
    val flightNumber: Int
) {

    companion object {
        fun getSampleFirstStageSummary(flightNumber: Int) =
            FirstStageSummary(flightNumber = flightNumber)
    }
}