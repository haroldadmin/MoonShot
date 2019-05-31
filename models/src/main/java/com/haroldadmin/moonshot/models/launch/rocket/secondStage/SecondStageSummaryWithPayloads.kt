package com.haroldadmin.moonshot.models.launch.rocket.secondStage

import androidx.room.Embedded
import androidx.room.Relation
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.payload.Payload

data class SecondStageSummaryWithPayloads(
    @Embedded
    val secondStageSummary: SecondStageSummary,
    @Relation(
        entity = Payload::class,
        parentColumn = "second_stage_summary_id",
        entityColumn = "second_stage_summary_id"
    ) val payloads: List<Payload>
)