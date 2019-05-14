package com.haroldadmin.moonshot.database.capsule

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface CapsuleDao {

    @Query("SELECT * FROM capsules")
    fun getAllCapsules(): Flowable<List<Capsule>>

    @Query("SELECT * FROM capsules WHERE capsule_serial = :serial")
    fun getCapsule(serial: String): Flowable<Capsule>

    @Query("SELECT * FROM capsules WHERE capsule_serial = :serial")
    fun getCapsuleWithMissionSummaries(serial: String): Flowable<CapsulesWithMissionSummaries>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCapsule(capsule: Capsule): Completable

    @Delete
    fun deleteCapsule(capsule: Capsule): Completable
}