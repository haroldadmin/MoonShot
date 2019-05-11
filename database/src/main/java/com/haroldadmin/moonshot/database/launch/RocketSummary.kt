package com.haroldadmin.moonshot.database.launch

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rocket_summary")
data class RocketSummary(
    @PrimaryKey
    @ColumnInfo(name = "rocket_id")
    val rocketId: String,
    @ColumnInfo(name = "rocket_name")
    val rocketName: String,
    @ColumnInfo(name = "rocket_type")
    val rocketType: String,
    @Embedded
    val firstStageSummary: FirstStageSummary,
    @Embedded
    val secondStageSummary: SecondStageSummary
)

