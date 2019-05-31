package com.haroldadmin.moonshot.database.capsule

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.haroldadmin.moonshot.database.BaseDao
import com.haroldadmin.moonshot.models.capsule.Capsule
import com.haroldadmin.moonshot.models.capsule.CapsulesWithMissionSummaries

@Dao
abstract class CapsuleDao : BaseDao<Capsule> {

    @Query("SELECT * FROM capsules")
    abstract suspend fun getAllCapsules(): List<Capsule>

    @Query("SELECT * FROM capsules WHERE capsule_serial = :serial")
    abstract suspend fun getCapsule(serial: String): Capsule

    @Query("SELECT * FROM capsules WHERE capsule_serial = :serial")
    @Transaction
    abstract suspend fun getCapsuleWithMissionSummaries(serial: String): CapsulesWithMissionSummaries
}