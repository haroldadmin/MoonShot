package com.haroldadmin.moonshot.database.launch.rocket.first_stage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface FirstStageSummaryDao {

    @Query("SELECT * FROM first_stage_summaries")
    fun getAllFirstStageSummaries(): Flowable<List<FirstStageSummary>>

    @Query("SELECT * FROM first_stage_summaries WHERE first_stage_summary_id = :id")
    fun getFirstStageSummary(id: Int): Flowable<FirstStageSummary>

    @Query("SELECT * FROM core_summaries WHERE core_serial = :serial")
    fun getCoreSummary(serial: String): Flowable<CoreSummary>

    @Query("SELECT * FROM first_stage_summaries WHERE first_stage_summary_id = :id")
    fun getFirstStageWithCoreSummaries(id: Int): Flowable<FirstStageWithCoreSummaries>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveFirstStageSummary(firstStageSummary: FirstStageSummary): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCoreSummary(coreSummary: CoreSummary): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCoreSummaries(vararg coreSummary: CoreSummary): Completable

    @Delete
    fun deleteFirstStageSummary(firstStageSummary: FirstStageSummary): Completable

    @Delete
    fun deleteCoreSummary(coreSummary: CoreSummary): Completable
}