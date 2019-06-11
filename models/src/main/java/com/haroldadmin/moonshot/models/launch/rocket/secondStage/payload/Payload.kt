package com.haroldadmin.moonshot.models.launch.rocket.secondStage.payload

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity

@Entity(
    tableName = "payloads",
    primaryKeys = ["launch_flight_number", "payload_id"]
)
data class Payload(
    @ColumnInfo(name = "launch_flight_number") val flightNumber: Int,
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
    @Embedded val orbitParams: OrbitParams
) {
    companion object {
        fun getSamplePayload(flightNumber: Int): Payload {
            return Payload(
                flightNumber = flightNumber,
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
                orbitParams = OrbitParams.getSampleOrbitParams()
            )
        }
    }
}