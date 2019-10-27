package com.haroldadmin.moonshot.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.haroldadmin.moonshot.models.ApplicationInfo

@Dao
interface ApplicationInfoDao {
    @Query("""
        SELECT *
        FROM applicationinfo
        WHERE id = "moonshot"
    """)
    suspend fun applicationInfo(): ApplicationInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(info: ApplicationInfo)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(info: ApplicationInfo)
}