package com.haroldadmin.moonshot.database.launch.rocket.second_stage

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.haroldadmin.moonshot.database.BaseDao
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.SecondStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.SecondStageSummaryWithPayloads
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.payload.Payload

@Dao
abstract class SecondStageSummaryDao : BaseDao<SecondStageSummary> {

    @Query("SELECT * FROM second_stage_summaries")
    abstract suspend fun getAllSecondStageSummaries(): List<SecondStageSummary>

    @Query("SELECT * FROM second_stage_summaries WHERE second_stage_summary_id = :id")
    abstract suspend fun getSecondStageSummary(id: Int): SecondStageSummary

    @Query("SELECT * FROM payloads WHERE payload_id = :id")
    abstract suspend fun getPayload(id: String): Payload

    @Query("SELECT * FROM second_stage_summaries WHERE second_stage_summary_id = :id")
    @Transaction
    abstract suspend fun getSecondStageWithPayloads(id: Int): SecondStageSummaryWithPayloads
}