package com.haroldadmin.moonshot.database.launch

import androidx.room.Embedded
import androidx.room.Relation

data class SecondStageSummaryWithPayloads(
    @Embedded
    val secondStageSummary: SecondStageSummary,
    @Relation(
        entity = Payload::class,
        parentColumn = "second_stage_summary_id",
        entityColumn = "second_stage_summary_id"
    ) val payloads: List<Payload>
)