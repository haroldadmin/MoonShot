package com.haroldadmin.moonshot.database.launch.rocket.first_stage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.CoreSummary
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.FirstStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.FirstStageWithCoreSummaries

@Dao
interface FirstStageSummaryDao {

    @Query("SELECT * FROM first_stage_summaries")
    suspend fun getAllFirstStageSummaries(): List<FirstStageSummary>

    @Query("SELECT * FROM first_stage_summaries WHERE first_stage_summary_id = :id")
    suspend fun getFirstStageSummary(id: Int): FirstStageSummary

    @Query("SELECT * FROM core_summaries WHERE core_serial = :serial")
    suspend fun getCoreSummary(serial: String): CoreSummary

    @Query("SELECT * FROM first_stage_summaries WHERE first_stage_summary_id = :id")
    @Transaction
    suspend fun getFirstStageWithCoreSummaries(id: Int): FirstStageWithCoreSummaries

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFirstStageSummary(firstStageSummary: FirstStageSummary): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCoreSummary(coreSummary: CoreSummary)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCoreSummaries(vararg coreSummary: CoreSummary)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCoreSummaries(coreSummaries: List<CoreSummary>)

    @Delete
    suspend fun deleteFirstStageSummary(firstStageSummary: FirstStageSummary)

    @Delete
    suspend fun deleteCoreSummary(coreSummary: CoreSummary)
}