package com.haroldadmin.moonshot.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.haroldadmin.moonshot.models.Mission
import kotlinx.coroutines.runBlocking

@Dao
abstract class MissionDao: BaseDao<Mission> {

    @Query("""
        SELECT *
        FROM mission
        WHERE mission_id = :missionId
    """)
    abstract fun forId(missionId: String): Mission?

    @Query("""
        SELECT *
        FROM mission
        ORDER BY mission_name ASC
        LIMIT :limit
        OFFSET :offset
    """)
    abstract fun all(limit: Int, offset: Int = 0): List<Mission>

    @Query("""
        DELETE FROM mission
    """)
    abstract suspend fun clearTable()

    @Transaction
    open fun synchronizeBlocking(allMissions: List<Mission>) = runBlocking {
        clearTable()
        saveAll(allMissions)
    }

}