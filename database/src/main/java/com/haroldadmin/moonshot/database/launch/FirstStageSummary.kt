package com.haroldadmin.moonshot.database.launch

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "first_stage_summary", indices = [Index("first_stage_summary_id")])
data class FirstStageSummary(
    @ColumnInfo(name = "first_stage_summary_id")
    @PrimaryKey(autoGenerate = true) val id: Int = -1
)

