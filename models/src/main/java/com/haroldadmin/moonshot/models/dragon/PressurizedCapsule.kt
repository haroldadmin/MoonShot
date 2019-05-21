package com.haroldadmin.moonshot.models.dragon

import androidx.room.Embedded
import com.haroldadmin.moonshot.models.common.Volume

data class PressurizedCapsule(
    @Embedded
    val payloadVol: Volume
)