package com.haroldadmin.moonshot.database.launch.rocket.second_stage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haroldadmin.moonshot.database.launch.rocket.second_stage.payload.Payload
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface SecondStageSummaryDao {

    @Query("SELECT * FROM second_stage_summaries")
    fun getAllSecondStageSummaries(): Flowable<List<SecondStageSummary>>

    @Query("SELECT * FROM second_stage_summaries WHERE second_stage_summary_id = :id")
    fun getSecondStageSummary(id: Int): Flowable<SecondStageSummary>

    @Query("SELECT * FROM payloads WHERE payload_id = :id")
    fun getPayload(id: String): Flowable<Payload>

    @Query("SELECT * FROM second_stage_summaries WHERE second_stage_summary_id = :id")
    fun getSecondStageWithPayloads(id: Int): Flowable<SecondStageSummaryWithPayloads>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSecondStageSummary(secondStageSummary: SecondStageSummary): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePayload(payload: Payload): Completable

    @Delete
    fun deleteSecondStageSummary(secondStageSummary: SecondStageSummary): Completable

    @Delete
    fun deletePayload(payload: Payload): Completable
}