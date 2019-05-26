package com.haroldadmin.moonshot.models.rocket.second_stage

import androidx.room.Embedded
import com.haroldadmin.moonshot.models.common.Length

data class CompositeFairing (
    @Embedded
    val height: Length,
    @Embedded
    val diameter: Length
)