package com.haroldadmin.moonshot.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.models.common.Length
import com.haroldadmin.moonshot.models.common.Mass
import com.haroldadmin.moonshot.models.common.Volume

@Entity
data class Dragon(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val dragonId: String,
    @ColumnInfo(name = "name")
    val dragonName: String,
    @ColumnInfo(name = "type")
    val dragonType: String,
    @ColumnInfo(name = "active")
    val active: Boolean,
    @ColumnInfo(name = "crew_capacity")
    val crewCapacity: Int,
    @ColumnInfo(name = "sidewall_angle_deg")
    val sideWallAngleDeg: Double,
    @ColumnInfo(name = "orbit_duration_yr")
    val orbitDurationYear: Double,
    @ColumnInfo(name = "dry_mass_kg")
    val dryMassKg: Double,
    @ColumnInfo(name = "dry_mass_lb")
    val dryMassLb: Double,
    @ColumnInfo(name = "first_flight")
    val firstFlight: String?,
    @Embedded
    val heatShield: HeatShield,
    @Embedded(prefix = "launch_payload_")
    val launchPayloadMass: Mass,
    @Embedded(prefix = "launch_payload_")
    val launchPayloadVol: Volume,
    @Embedded(prefix = "return_payload_")
    val returnPayloadMass: Mass,
    @Embedded(prefix = "return_payload_")
    val returnPayloadVol: Volume,
    @Embedded(prefix = "pressurized_capsule")
    val pressurizedCapsule: PressurizedCapsule,
    @Embedded
    val trunk: Trunk,
    @Embedded(prefix = "height_w_trunk_")
    val heightWTrunk: Length,
    @Embedded(prefix = "diameter_")
    val diameter: Length,
    @ColumnInfo(name = "wikipedia")
    val wikipedia: String,
    @ColumnInfo(name = "description")
    val description: String
)

data class HeatShield(
    val material: String,
    val sizeMeters: Double,
    val tempDegrees: Double,
    val devPartner: String
)

data class PressurizedCapsule(
    @Embedded
    val payloadVol: Volume
)

data class Trunk(
    @Embedded
    val trunkVolume: Volume,
    @Embedded
    val cargo: Cargo
)

data class Cargo(
    val solarArray: Int,
    val unpressurizedCargo: Boolean
)
