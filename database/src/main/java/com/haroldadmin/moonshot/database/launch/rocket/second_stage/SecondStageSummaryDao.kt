package com.haroldadmin.moonshot.database.launch.rocket.second_stage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.payload.Payload
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.SecondStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.SecondStageSummaryWithPayloads

@Dao
interface SecondStageSummaryDao {

    @Query("SELECT * FROM second_stage_summaries")
    suspend fun getAllSecondStageSummaries(): List<SecondStageSummary>

    @Query("SELECT * FROM second_stage_summaries WHERE second_stage_summary_id = :id")
    suspend fun getSecondStageSummary(id: Int): SecondStageSummary

    @Query("SELECT * FROM payloads WHERE payload_id = :id")
    suspend fun getPayload(id: String): Payload

    @Query("SELECT * FROM second_stage_summaries WHERE second_stage_summary_id = :id")
    suspend fun getSecondStageWithPayloads(id: Int): SecondStageSummaryWithPayloads

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSecondStageSummary(secondStageSummary: SecondStageSummary)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePayload(payload: Payload)

    @Delete
    suspend fun deleteSecondStageSummary(secondStageSummary: SecondStageSummary)

    @Delete
    suspend fun deletePayload(payload: Payload)
}