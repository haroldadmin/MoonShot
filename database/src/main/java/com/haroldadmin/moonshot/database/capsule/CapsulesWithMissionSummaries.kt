package com.haroldadmin.moonshot.database.capsule

import androidx.room.Embedded
import androidx.room.Relation
import com.haroldadmin.moonshot.database.common.MissionSummary

data class CapsulesWithMissionSummaries (
    @Embedded
    val capsule: Capsule,
    @Relation(
        entity = MissionSummary::class,
        entityColumn = "capsule_serial",
        parentColumn = "capsule_serial"
    )
    val missions: List<MissionSummary>
)