package com.haroldadmin.moonshot.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.haroldadmin.moonshot.models.LaunchPad
import kotlinx.coroutines.runBlocking

@Dao
abstract class LaunchPadDao : BaseDao<LaunchPad> {

    @Query("""
        SELECT *
        FROM launchpad
        WHERE site_id = :siteId
        ORDER BY id
        LIMIT 1
    """)
    abstract suspend fun one(siteId: String): LaunchPad?

    @Query("""
        SELECT *
        FROM launchpad
        ORDER BY site_name_long
        LIMIT :limit
        OFFSET :offset
    """)
    abstract suspend fun all(limit: Int, offset: Int = 0): List<LaunchPad>

    @Query("""
        SELECT *
        FROM launchpad
        WHERE site_name_long LIKE :query
        ORDER BY site_name_long
        LIMIT :limit
        OFFSET :offset
    """)
    abstract suspend fun forQuery(query: String, limit: Int, offset: Int = 0): List<LaunchPad>

    @Query("""
        DELETE FROM launchpad
    """)
    abstract suspend fun clearTable()

    @Transaction
    open fun synchronizeBlocking(allLaunchPads: List<LaunchPad>) = runBlocking {
        clearTable()
        saveAll(allLaunchPads)
    }
}