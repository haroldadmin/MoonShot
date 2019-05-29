package com.haroldadmin.moonshot.models.rocket

import androidx.room.Embedded
import androidx.room.Relation

data class RocketWithPayloadWeights(
    @Embedded
    val rocket: Rocket,
    @Relation(
        entity = PayloadWeight::class,
        entityColumn = "rocket_id",
        parentColumn = "rocket_id"
    )
    val payloadWeights: List<PayloadWeight>
)