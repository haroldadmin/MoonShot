package com.haroldadmin.moonshot.database.launch

import androidx.room.Embedded
import androidx.room.Relation

data class RocketSummaryWithFairings(
    @Embedded
    val rocketSummary: RocketSummary,
    @Relation(entity = Fairings::class, parentColumn = "rocket_id", entityColumn = "rocket_summary_id")
    val fairing: List<Fairings>
)