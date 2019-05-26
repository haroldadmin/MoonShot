package com.haroldadmin.moonshot.database.core

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haroldadmin.moonshot.models.core.Core
import com.haroldadmin.moonshot.models.core.CoreWithMissionSummaries

@Dao
interface CoreDao {
    @Query("SELECT * FROM cores")
    suspend fun getAllCores(): List<Core>

    @Query("SELECT * FROM cores WHERE core_serial = :serial")
    suspend fun getCore(serial: String): Core

    @Query("SELECT * FROM cores WHERE core_serial = :serial")
    suspend fun getCoreWtihMissionSummaries(serial: String): CoreWithMissionSummaries

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCore(core: Core)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCores(vararg cores: Core)

    @Delete
    suspend fun deleteCore(core: Core)

}