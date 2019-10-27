package com.haroldadmin.moonshot.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.LaunchPictures
import kotlinx.coroutines.runBlocking

@Dao
abstract class LaunchDao: BaseDao<Launch> {

    @Query("""
        SELECT *
        FROM launch
        WHERE flight_number = :flightNumber
    """)
    abstract suspend fun details(flightNumber: Int): Launch?

    @Query("""
        SELECT *
        FROM launch
        ORDER BY flight_number ASC
        LIMIT :limit
        OFFSET :offset
    """)
    abstract suspend fun all(limit: Int, offset: Int = 0): List<Launch>

    @Query("""
        SELECT *
        FROM launch
        WHERE upcoming = 1
        ORDER BY flight_number ASC
        LIMIT :limit
        OFFSET :offset
    """)
    abstract suspend fun upcoming(limit: Int, offset: Int = 0): List<Launch>

    @Query("""
        SELECT *
        FROM launch
        WHERE upcoming = 1 AND launch_date_utc <= :until
        ORDER BY flight_number ASC
        LIMIT :limit
        OFFSET :offset
    """)
    abstract suspend fun upcoming(until: Long, limit: Int, offset: Int = 0): List<Launch>

    @Query("""
        SELECT *
        FROM launch
        WHERE upcoming = 0
        ORDER BY flight_number DESC
        LIMIT :limit
        OFFSET :offset
    """)
    abstract suspend fun recent(limit: Int, offset: Int = 0): List<Launch>

    @Query("""
        SELECT *
        FROM launch
        WHERE siteId = :siteId
        ORDER BY flight_number ASC
        LIMIT :limit
        OFFSET :offset
    """)
    abstract suspend fun forLaunchPad(siteId: String, limit: Int, offset: Int = 0): List<Launch>

    @Query("""
        SELECT *
        FROM launch
        WHERE siteId = :siteId AND upcoming = :isUpcoming
        ORDER BY flight_number ASC
        LIMIT :limit
        OFFSET :offset
    """)
    abstract suspend fun forLaunchPad(siteId: String, isUpcoming: Boolean, limit: Int, offset: Int = 0): List<Launch>

    @Query("""
        SELECT flickrImages
        FROM launch
        WHERE flight_number = :flightNumber
    """)
    // Returned as LaunchPictures instead of List<String> to tell Room to use a TypeConverter for this Query
    abstract suspend fun pictures(flightNumber: Int): LaunchPictures?

    @Query("""
        SELECT *
        FROM launch
        WHERE mission_name LIKE :query
        ORDER BY mission_name
        LIMIT :limit
        OFFSET :offset
    """)
    abstract suspend fun forQuery(query: String, limit: Int, offset: Int = 0): List<Launch>

    @Query("""
        SELECT *
        FROM launch
        WHERE upcoming = 1
        ORDER BY flight_number ASC
        LIMIT 1
    """)
    abstract suspend fun next(): Launch?

    @Query("""
        DELETE FROM launch
    """)
    abstract suspend fun clearTable()

    @Transaction
    open fun synchronizeBlocking(allLaunches: List<Launch>) = runBlocking {
        clearTable()
        saveAll(allLaunches)
    }
}