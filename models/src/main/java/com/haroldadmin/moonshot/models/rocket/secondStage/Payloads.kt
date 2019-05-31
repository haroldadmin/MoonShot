package com.haroldadmin.moonshot.models.rocket.secondStage

import androidx.room.Embedded

data class Payloads(
    val option1: String?,
    val option2: String?,
    @Embedded(prefix = "composite_fairing_")
    val compositeFairing: CompositeFairing?
)