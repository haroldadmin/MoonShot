package com.haroldadmin.moonshot.database.launch.rocket

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haroldadmin.moonshot.database.launch.rocket.first_stage.FirstStageWithCoreSummaries
import com.haroldadmin.moonshot.database.launch.rocket.second_stage.SecondStageSummary
import com.haroldadmin.moonshot.database.launch.rocket.second_stage.SecondStageSummaryWithPayloads
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface RocketSummaryDao {

    @Query("SELECT * FROM rocket_summaries")
    fun getAllRocketSummaries(): Flowable<List<RocketSummary>>

    @Query("SELECT * FROM rocket_summaries WHERE rocket_id = :id")
    fun getRocketSummary(id: String): Flowable<RocketSummary>

    @Query("SELECT * FROM first_stage_summaries WHERE rocket_id = :id")
    fun getFirstStage(id: String): Flowable<FirstStageWithCoreSummaries>

    @Query("SELECT * FROM second_stage_summaries WHERE rocket_id = :id")
    fun getSecondStage(id: String): Flowable<SecondStageSummaryWithPayloads>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveRocketSummary(rocketSummary: RocketSummary): Completable

    @Delete
    fun deleteRocketSummary(rocketSummary: RocketSummary): Completable
}