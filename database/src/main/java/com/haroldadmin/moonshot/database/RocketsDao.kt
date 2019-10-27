package com.haroldadmin.moonshot.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.haroldadmin.moonshot.models.Rocket
import com.haroldadmin.moonshot.models.launch.Launch
import kotlinx.coroutines.runBlocking

@Dao
abstract class RocketsDao : BaseDao<Rocket> {

    @Query("""
        SELECT *
        FROM rocket
        ORDER BY rocket_name
        LIMIT :limit
        OFFSET :offset
    """)
    abstract suspend fun all(limit: Int, offset: Int = 0): List<Rocket>

    @Query("""
        SELECT *
        FROM rocket
        WHERE rocket_id = :rocketId
    """)
    abstract suspend fun one(rocketId: String): Rocket?

    @Query("""
        SELECT *
        FROM launch
        WHERE rocket_id = :rocketId AND upcoming = 0
        ORDER BY flight_number DESC
        LIMIT :limit
        OFFSET :offset
    """)
    abstract suspend fun launchesForRocket(rocketId: String, limit: Int, offset: Int = 0): List<Launch>

    @Query("""
        SELECT *
        FROM rocket
        WHERE rocket_name LIKE :query
        ORDER BY rocket_name
        LIMIT :limit
        OFFSET :offset
    """)
    abstract suspend fun forQuery(query: String, limit: Int, offset: Int = 0): List<Rocket>

    @Query("""
        DELETE FROM rocket
    """)
    abstract suspend fun clearTable()

    @Transaction
    open fun synchronizeBlocking(allRockets: List<Rocket>) = runBlocking {
        clearTable()
        saveAll(allRockets)
    }
}