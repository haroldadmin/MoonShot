package com.haroldadmin.moonshot.database.launch

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "fairings",
    foreignKeys = [
        ForeignKey(
            entity = RocketSummary::class,
            parentColumns = ["rocket_id"],
            childColumns = ["rocket_summary_id"]
        )
    ],
    indices = [Index("rocket_summary_id")]
)
data class Fairings(
    @ColumnInfo(name = "reused") val reused: Boolean,
    @ColumnInfo(name = "recovery_attempt") val recoveryAttempt: Boolean,
    @ColumnInfo(name = "recovered") val recovered: Boolean,
    @ColumnInfo(name = "ship") val ship: String?,
    @ColumnInfo(name = "rocket_summary_id") val rocketSummaryId: String
) {

    @PrimaryKey(autoGenerate = true) var id: Int? = null

}