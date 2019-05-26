package com.haroldadmin.moonshot.models.rocket.first_stage

import androidx.room.Embedded
import com.haroldadmin.moonshot.models.common.Thrust

data class FirstStage (
    val reusable: Boolean,
    val engines: Int,
    val fuelAmountTons: Double,
    val burnTimeSec: Double,
    @Embedded(prefix = "thrust_sea_level")
    val thrustSeaLevel: Thrust,
    @Embedded(prefix = "thrust_vacuum")
    val thrustVacuum: Thrust
)