package com.haroldadmin.moonshot.models.rocket.first_stage

import androidx.room.Embedded
import com.haroldadmin.moonshot.models.common.Thrust

data class FirstStage (
    val reusable: Boolean,
    val engines: Int,
    val fuelAmountTons: Double,
    val burnTimeSec: Double,
    @Embedded
    val thrustSeaLevel: Thrust,
    @Embedded
    val thrustVacuum: Thrust
)