package com.haroldadmin.moonshot.models.launch.rocket.second_stage.payload

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.SecondStageSummary

@Entity(
    tableName = "payloads",
    foreignKeys = [
        ForeignKey(
            entity = SecondStageSummary::class,
            parentColumns = ["second_stage_summary_id"],
            childColumns = ["second_stage_summary_id"],
            onDelete = ForeignKey.CASCADE
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
    @ColumnInfo(name = "nationality") val nationality: String?,
    @ColumnInfo(name = "manufacturer") val manufacturer: String?,
    @ColumnInfo(name = "payload_type") val payloadType: String,
    @ColumnInfo(name = "payload_mass_kg") val payloadMassKg: Double?,
    @ColumnInfo(name = "payload_mass_lbs") val payloadMassLbs: Double?,
    @ColumnInfo(name = "orbit") val orbit: String,
    @Embedded val orbitParams: OrbitParams,
    @ColumnInfo(name = "second_stage_summary_id") val secondStageSummaryId: Int
) {
    companion object {
        fun getSamplePayload(secondStageSummaryId: Int): Payload {
            return Payload(
                id = "id",
                noradId = listOf(1),
                reused = false,
                customers = listOf(""),
                nationality = "Country",
                manufacturer = "SpaceX",
                payloadType = "Type",
                payloadMassKg = 0.0,
                payloadMassLbs = 0.0,
                orbit = "Orbit",
                orbitParams = OrbitParams.getSampleOrbitParams(),
                secondStageSummaryId = secondStageSummaryId
            )
        }
    }
}