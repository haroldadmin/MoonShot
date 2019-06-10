package com.haroldadmin.moonshot.models.launch.rocket.firstStage

import androidx.room.Embedded
import androidx.room.Relation

data class FirstStageWithCoreSummaries(
    @Embedded val firstStageSummary: FirstStageSummary?,
    @Relation(
        entity = CoreSummary::class,
        parentColumn = "launch_flight_number",
        entityColumn = "launch_flight_number"
    )
    val cores: List<CoreSummary>
)