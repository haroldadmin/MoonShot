package com.haroldadmin.moonshot.database.capsule

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haroldadmin.moonshot.models.capsule.Capsule
import com.haroldadmin.moonshot.models.capsule.CapsulesWithMissionSummaries

@Dao
interface CapsuleDao {

    @Query("SELECT * FROM capsules")
    suspend fun getAllCapsules(): List<Capsule>

    @Query("SELECT * FROM capsules WHERE capsule_serial = :serial")
    suspend fun getCapsule(serial: String): Capsule

    @Query("SELECT * FROM capsules WHERE capsule_serial = :serial")
    suspend fun getCapsuleWithMissionSummaries(serial: String): CapsulesWithMissionSummaries

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCapsule(capsule: Capsule)

    @Delete
    suspend fun deleteCapsule(capsule: Capsule)
}