package com.haroldadmin.moonshot.database.launch.rocket.first_stage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "core_summaries",
    foreignKeys = [
        ForeignKey(
            entity = FirstStageSummary::class,
            parentColumns = ["first_stage_summary_id"],
            childColumns = ["first_stage_summary_id"]
        )
    ],
    indices = [Index("first_stage_summary_id")]
)
data class CoreSummary(
    @PrimaryKey
    @ColumnInfo(name = "core_serial") val serial: String,
    @ColumnInfo(name = "flight") val flight: Int,
    @ColumnInfo(name = "block") val block: Int?,
    @ColumnInfo(name = "gridfins") val gridfins: Boolean,
    @ColumnInfo(name = "legs") val legs: Boolean,
    @ColumnInfo(name = "reused") val reused: Boolean,
    @ColumnInfo(name = "land_success") val landSuccess: Boolean?,
    @ColumnInfo(name = "landing_intent") val landingIntent: Boolean,
    @ColumnInfo(name = "landing_type") val landingType: String?,
    @ColumnInfo(name = "landing_vehicle") val landingVehicle: String?,
    @ColumnInfo(name = "first_stage_summary_id") val firstStageSummaryId: Int
) {

    companion object {
        internal fun getSampleCoreSummary(firstStageSummaryId: Int): CoreSummary =
            CoreSummary(
                "B1042",
                0,
                4,
                true,
                true,
                true,
                true,
                true,
                "",
                "",
                firstStageSummaryId
            )
    }

}