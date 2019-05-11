package com.haroldadmin.moonshot.database.launch

import androidx.room.Embedded
import androidx.room.Relation

data class FirstStageSummaryWithCoreSummaries(
    @Embedded val firstStageSummary: FirstStageSummary,
    @Relation(
        entity = CoreSummary::class,
        parentColumn = "first_stage_summary_id",
        entityColumn = "first_stage_summary_id"
    )
    val cores: List<CoreSummary>
)