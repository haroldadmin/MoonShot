package com.haroldadmin.moonshot.database.launch.rocket.second_stage.payload

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.database.launch.rocket.second_stage.SecondStageSummary

@Entity(
    tableName = "payloads",
    foreignKeys = [
        ForeignKey(
            entity = SecondStageSummary::class,
            parentColumns = ["second_stage_summary_id"],
            childColumns = ["second_stage_summary_id"]
        )
    ],
    indices = [Index("second_stage_summary_id")]
)
data class Payload(
    @PrimaryKey
    @ColumnInfo(name = "payload_id") val id: String,
    @ColumnInfo(name = "norad_id") val noradId: List<Int>,
    @ColumnInfo(name = "reused") val reused: Boolean,
    @ColumnInfo(name = "customers") val customers: List<String>,
    @ColumnInfo(name = "nationality") val nationality: String,
    @ColumnInfo(name = "manufacturer") val manufacturer: String,
    @ColumnInfo(name = "payload_type") val payloadType: String,
    @ColumnInfo(name = "payload_mass_kg") val payloadMassKg: Double,
    @ColumnInfo(name = "payload_mass_lbs") val payloadMassLbs: Double,
    @ColumnInfo(name = "orbit") val orbit: String,
    @Embedded val orbitParams: OrbitParams,
    @ColumnInfo(name = "second_stage_summary_id") val secondStageSummaryId: Int
)