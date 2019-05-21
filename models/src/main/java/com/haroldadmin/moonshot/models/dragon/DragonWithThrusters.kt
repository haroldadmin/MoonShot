package com.haroldadmin.moonshot.models.dragon

import androidx.room.Embedded
import androidx.room.Relation

data class DragonWithThrusters(
    @Embedded
    val dragon: Dragon,
    @Relation(
        entity = Thruster::class,
        parentColumn = "dragon_id",
        entityColumn = "dragon_id"
    )
    val thrusters: List<Thruster>
)