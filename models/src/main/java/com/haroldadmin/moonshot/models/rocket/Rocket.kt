package com.haroldadmin.moonshot.models.rocket

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.models.common.Length
import com.haroldadmin.moonshot.models.common.Mass
import com.haroldadmin.moonshot.models.common.Thrust
import com.haroldadmin.moonshot.models.rocket.firstStage.FirstStage
import com.haroldadmin.moonshot.models.rocket.secondStage.CompositeFairing
import com.haroldadmin.moonshot.models.rocket.secondStage.Payloads
import com.haroldadmin.moonshot.models.rocket.secondStage.SecondStage

@Entity(tableName = "rockets")
data class Rocket(
    @ColumnInfo(name = "rocket_id")
    @PrimaryKey
    val rocketId: String,
    @ColumnInfo(name = "rocket_name")
    val rocketName: String,
    @ColumnInfo(name = "rocket_type")
    val rocketType: String,
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "active")
    val active: Boolean,
    @ColumnInfo(name = "stages")
    val stages: Int,
    @ColumnInfo(name = "boosters")
    val boosters: Int,
    @ColumnInfo(name = "cost_per_launch")
    val costPerLaunch: Long,
    @ColumnInfo(name = "success_rate")
    val successRatePercentage: Double,
    @ColumnInfo(name = "first_flight")
    val firstFlight: String,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "company")
    val company: String,
    @Embedded(prefix = "rocket_height_")
    val height: Length,
    @Embedded(prefix = "rocket_diameter_")
    val diameter: Length,
    @Embedded(prefix = "rocket_mass_")
    val mass: Mass,
    @Embedded(prefix = "first_stage_")
    val firstStage: FirstStage,
    @Embedded(prefix = "second_stage_")
    val secondStage: SecondStage,
    @Embedded(prefix = "engines_")
    val engines: Engine,
    @Embedded(prefix = "landing_legs_")
    val landingLegs: LandingLegs,
    @ColumnInfo(name = "wikipedia")
    val wikipedia: String,
    @ColumnInfo(name = "description")
    val description: String
) {

    companion object {
        fun getSampleRocket(): Rocket {
            return Rocket(
                id = 2,
                active = true,
                stages = 2,
                boosters = 0,
                costPerLaunch = 50000000,
                successRatePercentage = 97.0,
                firstFlight = "2010-06-04",
                country = "United States",
                company = "SpaceX",
                height = Length(
                    meters = 70.0,
                    feet = 229.6
                ),
                diameter = Length(
                    meters = 3.7,
                    feet = 12.0
                ),
                mass = Mass(
                    kg = 549054.0,
                    lb = 1207920.0
                ),
                firstStage = FirstStage(
                    reusable = false,
                    engines = 9,
                    fuelAmountTons = 385.0,
                    burnTimeSec = 162.0,
                    thrustSeaLevel = Thrust(
                        kN = 7607.0,
                        lbf = 1710000.0
                    ),
                    thrustVacuum = Thrust(
                        kN = 8227.0,
                        lbf = 1949500.0
                    )
                ),
                secondStage = SecondStage(
                    engines = 1,
                    fuelAmountTons = 90.0,
                    burnTimeSec = 397.0,
                    thrust = Thrust(
                        kN = 934.0,
                        lbf = 210000.0
                    ),
                    payloads = Payloads(
                        option1 = "dragon1",
                        option2 = "composite fairing",
                        compositeFairing = CompositeFairing(
                            height = Length(
                                meters = 13.1,
                                feet = 43.0
                            ),
                            diameter = Length(
                                meters = 5.2,
                                feet = 17.1
                            )
                        )
                    )
                ),
                engines = Engine(
                    number = 9,
                    type = "merlin",
                    version = "1D+",
                    layout = "octaweb",
                    engineLossMax = 2,
                    propellant1 = "liquid oxygen",
                    propellant2 = "RP-1 kerosene",
                    thrustSeaLevel = Thrust(
                        kN = 845.0,
                        lbf = 190000.0
                    ),
                    thrustVacuum = Thrust(
                        kN = 914.0,
                        lbf = 205500.0
                    ),
                    thrustToWeight = 180.1
                ),
                landingLegs = LandingLegs(
                    number = 4,
                    material = "carbon fiber"
                ),
                wikipedia = "https://en.wikipedia.org/wiki/Falcon_9",
                description = "Falcon 9 is a two-stage rocket designed and manufactured by SpaceX for the reliable and safe transport of satellites and the Dragon spacecraft into orbit.",
                rocketId = "falcon9",
                rocketName = "Falcon 9",
                rocketType = "rocket"
            )
        }
    }
}

data class RocketMinimal(
    @ColumnInfo(name = "rocket_id")
    val rocketId: String,
    @ColumnInfo(name = "rocket_name")
    val rocketName: String,
    @ColumnInfo(name = "rocket_type")
    val rocketType: String,
    @ColumnInfo(name = "active")
    val active: Boolean,
    @ColumnInfo(name = "cost_per_launch")
    val costPerLaunch: Long,
    @ColumnInfo(name = "success_rate")
    val successRatePercentage: Double,
    @ColumnInfo(name = "description")
    val description: String
) {
    @Ignore
    val statusText = if (active) "Active" else "Inactive"
}

fun Rocket.toRocketMinimal(): RocketMinimal {
    return RocketMinimal(
        rocketId = this.rocketId,
        rocketName = this.rocketName,
        rocketType = this.rocketType,
        active = this.active,
        costPerLaunch = this.costPerLaunch,
        successRatePercentage = this.successRatePercentage,
        description = this.description
    )
}