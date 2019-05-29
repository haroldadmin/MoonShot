package com.haroldadmin.moonshot.models.dragon

import androidx.room.Embedded
import com.haroldadmin.moonshot.models.common.Volume

data class Trunk(
    @Embedded
    val trunkVolume: Volume,
    @Embedded
    val cargo: Cargo
)