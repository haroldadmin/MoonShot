package com.haroldadmin.moonshot.database.dragon

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.database.common.Length
import com.haroldadmin.moonshot.database.common.Mass
import com.haroldadmin.moonshot.database.common.Volume
import java.util.Date

@Entity(tableName = "dragons")
data class Dragon(
    @PrimaryKey
    @ColumnInfo(name = "dragon_id")
    val dragonId: String,
    @ColumnInfo(name = "dragon_name")
    val dragonName: String,
    @ColumnInfo(name = "dragon_type")
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
    val firstFlight: Date,
    @Embedded
    val heatShield: HeatShield,
    @Embedded(prefix="launch_payload_")
    val launchPayloadMass: Mass,
    @Embedded(prefix="launch_payload_")
    val launchPayloadVol: Volume,
    @Embedded(prefix="return_payload_")
    val returnPayloadMass: Mass,
    @Embedded(prefix="return_payload_")
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

