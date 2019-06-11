package com.haroldadmin.moonshot.models.launch.rocket.secondStage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "second_stage_summaries"
)
data class SecondStageSummary(
    @PrimaryKey
    @ColumnInfo(name = "launch_flight_number")
    val flightNumber: Int,
    @ColumnInfo(name = "block")
    val block: Int?
) {

    companion object {
        fun getSampleSecondStageSummary(flightNumber: Int) =
            SecondStageSummary(
                flightNumber,
                1
            )
    }
}
