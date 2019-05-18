package com.haroldadmin.moonshot.database.launch.rocket

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haroldadmin.moonshot.database.launch.rocket.first_stage.FirstStageWithCoreSummaries
import com.haroldadmin.moonshot.database.launch.rocket.second_stage.SecondStageSummaryWithPayloads

@Dao
interface RocketSummaryDao {

    @Query("SELECT * FROM rocket_summaries")
    suspend fun getAllRocketSummaries(): List<RocketSummary>

    @Query("SELECT * FROM rocket_summaries WHERE rocket_id = :id")
    suspend fun getRocketSummary(id: String): RocketSummary

    @Query("SELECT * FROM first_stage_summaries WHERE rocket_id = :id")
    suspend fun getFirstStage(id: String): FirstStageWithCoreSummaries

    @Query("SELECT * FROM second_stage_summaries WHERE rocket_id = :id")
    suspend fun getSecondStage(id: String): SecondStageSummaryWithPayloads

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRocketSummary(rocketSummary: RocketSummary)

    @Delete
    suspend fun deleteRocketSummary(rocketSummary: RocketSummary)
}