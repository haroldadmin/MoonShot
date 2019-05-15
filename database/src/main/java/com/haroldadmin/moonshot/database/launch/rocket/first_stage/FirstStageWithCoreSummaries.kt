package com.haroldadmin.moonshot.database.launch.rocket.first_stage

import androidx.room.Embedded
import androidx.room.Relation
import com.haroldadmin.moonshot.database.launch.rocket.first_stage.CoreSummary
import com.haroldadmin.moonshot.database.launch.rocket.first_stage.FirstStageSummary

data class FirstStageWithCoreSummaries(
    @Embedded val firstStageSummary: FirstStageSummary,
    @Relation(
        entity = CoreSummary::class,
        parentColumn = "first_stage_summary_id",
        entityColumn = "first_stage_summary_id"
    )
    val cores: List<CoreSummary>
)