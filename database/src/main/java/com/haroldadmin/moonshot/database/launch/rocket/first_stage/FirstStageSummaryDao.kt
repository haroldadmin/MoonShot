package com.haroldadmin.moonshot.database.launch.rocket.first_stage

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.haroldadmin.moonshot.database.BaseDao
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.CoreSummary
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.FirstStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.FirstStageWithCoreSummaries

@Dao
abstract class FirstStageSummaryDao : BaseDao<FirstStageSummary> {

    @Query("SELECT * FROM first_stage_summaries")
    abstract suspend fun getAllFirstStageSummaries(): List<FirstStageSummary>

    @Query("SELECT * FROM first_stage_summaries WHERE first_stage_summary_id = :id")
    abstract suspend fun getFirstStageSummary(id: Int): FirstStageSummary

    @Query("SELECT * FROM core_summaries WHERE core_serial = :serial")
    abstract suspend fun getCoreSummary(serial: String): CoreSummary

    @Query("SELECT * FROM first_stage_summaries WHERE first_stage_summary_id = :id")
    @Transaction
    abstract suspend fun getFirstStageWithCoreSummaries(id: Int): FirstStageWithCoreSummaries
}