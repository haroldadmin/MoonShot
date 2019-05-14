package com.haroldadmin.moonshot.database.common

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface MissionSummaryDao {

    @Query("SELECT * FROM mission_summaries")
    fun getAllMissionSummaries(): Flowable<List<MissionSummary>>

    @Insert
    fun saveMissionSummary(missionSummary: MissionSummary): Completable

    @Insert
    fun saveMissionSummaries(vararg missionSummary: MissionSummary): Completable

    @Delete
    fun deleteMissionSummary(missionSummary: MissionSummary): Completable

    @Delete
    fun deleteMissionSummaries(vararg missionSummaries: MissionSummary): Completable

}