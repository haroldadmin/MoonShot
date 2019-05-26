package com.haroldadmin.moonshot.models.core

import androidx.room.Embedded
import androidx.room.Relation
import com.haroldadmin.moonshot.models.common.MissionSummary

data class CoreWithMissionSummaries(
    @Embedded
    val core: Core,
    @Relation(
        entity = MissionSummary::class,
        parentColumn = "core_serial",
        entityColumn = "core_serial"
    )
    val missions: List<MissionSummary>
)