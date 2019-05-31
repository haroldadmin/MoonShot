package com.haroldadmin.moonshot.models.rocket.secondStage

import androidx.room.Embedded
import com.haroldadmin.moonshot.models.common.Length

data class CompositeFairing(
    @Embedded(prefix = "height")
    val height: Length,
    @Embedded(prefix = "diameter")
    val diameter: Length
)