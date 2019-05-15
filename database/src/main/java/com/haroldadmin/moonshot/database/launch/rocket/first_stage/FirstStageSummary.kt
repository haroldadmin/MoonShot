package com.haroldadmin.moonshot.database.launch.rocket.first_stage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.database.launch.rocket.RocketSummary

@Entity(
    tableName = "first_stage_summaries",
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
data class FirstStageSummary(
    @ColumnInfo(name = "rocket_id")
    val rocketId: String
) {
    @ColumnInfo(name = "first_stage_summary_id")
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    companion object {
        internal fun getSampleFirstStageSummary(rocketId: String) = FirstStageSummary(rocketId)
    }
}