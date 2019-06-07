package com.haroldadmin.moonshot.database.launch.rocket.firstStage

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.haroldadmin.moonshot.database.BaseDao
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.CoreSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.FirstStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.FirstStageWithCoreSummaries

@Dao
abstract class FirstStageSummaryDao : BaseDao<FirstStageSummary> {

    @Query("SELECT * FROM first_stage_summaries")
    abstract suspend fun getAllFirstStageSummaries(): List<FirstStageSummary>

    @Query("SELECT * FROM first_stage_summaries WHERE launch_flight_number = :flightNumber")
    abstract suspend fun getFirstStageSummary(flightNumber: Int): FirstStageSummary

    @Query("SELECT * FROM core_summaries WHERE core_serial = :serial")
    abstract suspend fun getCoreSummary(serial: String): CoreSummary

    @Query("SELECT * FROM first_stage_summaries WHERE launch_flight_number = :flightNumber")
    @Transaction
    abstract suspend fun getFirstStageWithCoreSummaries(flightNumber: Int): FirstStageWithCoreSummaries
}