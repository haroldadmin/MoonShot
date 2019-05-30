package com.haroldadmin.moonshot.models.launch.rocket.second_stage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary

@Entity(
    tableName = "second_stage_summaries",
    foreignKeys = [
        ForeignKey(
            entity = RocketSummary::class,
            parentColumns = ["rocket_id"],
            childColumns = ["rocket_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("rocket_id")]
)
data class SecondStageSummary(
    @ColumnInfo(name = "rocket_id")
    val rocketId: String,
    @ColumnInfo(name = "block")
    val block: Int?
) {

    @ColumnInfo(name = "second_stage_summary_id")
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    companion object {
        fun getSampleSecondStageSummary(rocketId: String) =
            SecondStageSummary(
                rocketId,
                1
            )
    }
}
