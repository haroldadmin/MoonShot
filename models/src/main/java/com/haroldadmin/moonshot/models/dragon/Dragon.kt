package com.haroldadmin.moonshot.models.dragon

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.models.common.Length
import com.haroldadmin.moonshot.models.common.Mass
import com.haroldadmin.moonshot.models.common.Volume

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
) {

    companion object {
        fun getSampleDragon(): Dragon {
            return Dragon(
                dragonId = "dragon1",
                dragonName = "Dragon 1",
                dragonType = "capsule",
                active = true,
                crewCapacity = 0,
                sideWallAngleDeg = 15.0,
                orbitDurationYear = 2.0,
                dryMassKg = 4200.0,
                dryMassLb = 9300.0,
                firstFlight = "2010-12-8",
                heatShield = HeatShield(
                    material = "PICA-X",
                    sizeMeters = 3.6,
                    tempDegrees = 3000.0,
                    devPartner = "NASA"
                ),
                launchPayloadMass = Mass(
                    kg = 6000.0,
                    lb = 13328.0
                ),
                launchPayloadVol = Volume(
                    cubicMeters = 25.0,
                    cubicFeet = 883.0
                ),
                returnPayloadMass = Mass(
                    kg = 3000.0,
                    lb = 6614.0
                ),
                returnPayloadVol = Volume(
                    cubicMeters = 11.0,
                    cubicFeet = 388.0
                ),
                pressurizedCapsule = PressurizedCapsule(
                    payloadVol = Volume(
                        cubicFeet = 388.0,
                        cubicMeters = 11.0
                    )
                ),
                trunk = Trunk(
                    trunkVolume = Volume(
                        cubicMeters = 14.0,
                        cubicFeet = 494.0
                    ),
                    cargo = Cargo(
                        solarArray = 2,
                        unpressurizedCargo = true
                    )
                ),
                heightWTrunk = Length(
                    meters = 7.2,
                    feet = 23.6
                ),
                diameter = Length(
                    meters = 3.7,
                    feet = 12.0
                ),
                wikipedia = "https://en.wikipedia.org/wiki/SpaceX_Dragon",
                description = "Dragon is a reusable spacecraft developed by SpaceX, an American private space " +
                        "transportation company based in Hawthorne, California. Dragon is launched into space by the " +
                        "SpaceX Falcon 9 two-stage-to-orbit launch vehicle. The Dragon spacecraft was originally " +
                        "designed for human travel, but so far has only been used to deliver cargo to the International " +
                        "Space Station (ISS)."
            )
        }
    }

}
