package com.haroldadmin.moonshot.models.launch.rocket.secondStage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary

@Entity(
    tableName = "second_stage_summaries",
    foreignKeys = [
        ForeignKey(
            entity = RocketSummary::class,
            parentColumns = ["launch_flight_number"],
            childColumns = ["launch_flight_number"],
            onDelete = ForeignKey.CASCADE
        )
    ]
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
