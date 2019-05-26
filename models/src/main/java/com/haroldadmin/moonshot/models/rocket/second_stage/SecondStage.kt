package com.haroldadmin.moonshot.models.rocket.second_stage

import androidx.room.Embedded
import com.haroldadmin.moonshot.models.common.Thrust

data class SecondStage (
    val engines: Int,
    val fuelAmountTons: Double,
    val burnTimeSec: Double,
    @Embedded
    val thrust: Thrust,
    @Embedded
    val payloads: Payloads
)