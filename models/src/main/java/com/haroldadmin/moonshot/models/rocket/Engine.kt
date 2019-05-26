package com.haroldadmin.moonshot.models.rocket

import androidx.room.Embedded
import com.haroldadmin.moonshot.models.common.Thrust

data class Engine (
    val number: Int,
    val type: String,
    val version: String,
    val layout: String,
    val engineLossMax: Int,
    val propellant1: String,
    val propellant2: String,
    @Embedded
    val thrustSeaLevel: Thrust,
    @Embedded
    val thrustVacuum: Thrust,
    val thrustToWeight: Double
)