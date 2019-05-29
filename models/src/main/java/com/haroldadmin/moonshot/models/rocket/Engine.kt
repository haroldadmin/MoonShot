package com.haroldadmin.moonshot.models.rocket

import androidx.room.Embedded
import com.haroldadmin.moonshot.models.common.Thrust

data class Engine(
    val number: Int,
    val type: String,
    val version: String,
    val layout: String?,
    val engineLossMax: Int?,
    val propellant1: String,
    val propellant2: String,
    @Embedded(prefix = "thrust_sea_level_")
    val thrustSeaLevel: Thrust,
    @Embedded(prefix = "thrust_vacuum_")
    val thrustVacuum: Thrust,
    val thrustToWeight: Double?
)