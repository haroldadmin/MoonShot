package com.haroldadmin.moonshot.models.launch.rocket.secondStage

import androidx.room.Embedded
import androidx.room.Relation
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.payload.Payload

data class SecondStageSummaryWithPayloads(
    @Embedded
    val secondStageSummary: SecondStageSummary,
    @Relation(
        entity = Payload::class,
        parentColumn = "launch_flight_number",
        entityColumn = "launch_flight_number"
    ) val payloads: List<Payload>
)