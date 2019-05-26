package com.haroldadmin.moonshot.database.launch.rocket

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.FirstStageWithCoreSummaries
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.SecondStageSummaryWithPayloads

@Dao
interface RocketSummaryDao {

    @Query("SELECT * FROM rocket_summaries")
    suspend fun getAllRocketSummaries(): List<RocketSummary>

    @Query("SELECT * FROM rocket_summaries WHERE rocket_id = :id")
    suspend fun getRocketSummary(id: String): RocketSummary

    @Query("SELECT * FROM first_stage_summaries WHERE rocket_id = :id")
    @Transaction
    suspend fun getFirstStage(id: String): FirstStageWithCoreSummaries

    @Query("SELECT * FROM second_stage_summaries WHERE rocket_id = :id")
    @Transaction
    suspend fun getSecondStage(id: String): SecondStageSummaryWithPayloads

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRocketSummary(rocketSummary: RocketSummary)

    @Delete
    suspend fun deleteRocketSummary(rocketSummary: RocketSummary)
}