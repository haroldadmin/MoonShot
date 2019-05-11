package com.haroldadmin.moonshot.database.launch

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "second_stage_summary")
data class SecondStageSummary(
    @ColumnInfo(name = "block")
    val block: Int,
    @ColumnInfo(name = "second_stage_summary_id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = -1
)

