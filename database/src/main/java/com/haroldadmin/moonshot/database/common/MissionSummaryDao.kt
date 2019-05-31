package com.haroldadmin.moonshot.database.common

import androidx.room.Dao
import androidx.room.Query
import com.haroldadmin.moonshot.database.BaseDao
import com.haroldadmin.moonshot.models.common.MissionSummary

@Dao
abstract class MissionSummaryDao : BaseDao<MissionSummary> {

    @Query("SELECT * FROM mission_summaries")
    abstract suspend fun getAllMissionSummaries(): List<MissionSummary>

}