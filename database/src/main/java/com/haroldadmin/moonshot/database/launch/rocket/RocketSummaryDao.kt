package com.haroldadmin.moonshot.database.launch.rocket

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.FirstStageWithCoreSummaries
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.SecondStageSummaryWithPayloads

@Dao
interface RocketSummaryDao {

    @Query("SELECT * FROM rocket_summaries WHERE launch_flight_number = :flightNumber")
    suspend fun getRocketSummaryForLaunch(flightNumber: Int): RocketSummary

    @Query("SELECT * FROM rocket_summaries")
    suspend fun getAllRocketSummaries(): List<RocketSummary>

    @Query("SELECT * FROM first_stage_summaries WHERE launch_flight_number = :flightNumber")
    @Transaction
    suspend fun getFirstStage(flightNumber: Int): FirstStageWithCoreSummaries

    @Query("SELECT * FROM second_stage_summaries WHERE launch_flight_number = :flightNumber")
    @Transaction
    suspend fun getSecondStage(flightNumber: Int): SecondStageSummaryWithPayloads

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRocketSummary(rocketSummary: RocketSummary)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRocketSummaries(rocketSummaries: List<RocketSummary>)

    @Delete
    suspend fun deleteRocketSummary(rocketSummary: RocketSummary)
}