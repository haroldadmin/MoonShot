package com.haroldadmin.moonshot.database.core

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.haroldadmin.moonshot.database.BaseDao
import com.haroldadmin.moonshot.models.core.Core
import com.haroldadmin.moonshot.models.core.CoreWithMissionSummaries

@Dao
abstract class CoreDao : BaseDao<Core> {
    @Query("SELECT * FROM cores")
    abstract suspend fun getAllCores(): List<Core>

    @Query("SELECT * FROM cores WHERE core_serial = :serial")
    abstract suspend fun getCore(serial: String): Core

    @Query("SELECT * FROM cores WHERE core_serial = :serial")
    @Transaction
    abstract suspend fun getCoreWtihMissionSummaries(serial: String): CoreWithMissionSummaries

}