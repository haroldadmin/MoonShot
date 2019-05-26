package com.haroldadmin.moonshot.models.rocket.second_stage

import androidx.room.Embedded
import com.haroldadmin.moonshot.models.common.Length

data class CompositeFairing (
    @Embedded(prefix = "height")
    val height: Length,
    @Embedded(prefix = "diameter")
    val diameter: Length
)