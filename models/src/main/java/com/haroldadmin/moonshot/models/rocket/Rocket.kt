package com.haroldadmin.moonshot.models.rocket

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.models.common.Length
import com.haroldadmin.moonshot.models.common.Mass
import com.haroldadmin.moonshot.models.rocket.first_stage.FirstStage
import com.haroldadmin.moonshot.models.rocket.second_stage.SecondStage

@Entity(tableName = "rockets")
data class Rocket (
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
)
