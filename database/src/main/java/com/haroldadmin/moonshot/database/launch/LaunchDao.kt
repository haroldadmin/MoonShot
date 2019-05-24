package com.haroldadmin.moonshot.database.launch

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary
import com.haroldadmin.moonshot.models.launch.Launch
import java.util.*

@Dao
interface LaunchDao {

    @Query("SELECT * FROM launches")
    suspend fun getAllLaunches(): List<Launch>

    @Query("SELECT * FROM launches WHERE flight_number = :flightNumber")
    suspend fun getLaunch(flightNumber: Int): Launch

    @Query("SELECT * FROM launches WHERE launch_date_utc >= :timestamp")
    suspend fun getUpcomingLaunches(timestamp: Long): List<Launch>

    @Query("SELECT * FROM launches WHERE launch_date_utc < :timestamp")
    suspend fun getPastLaunches(timestamp: Long): List<Launch>

    @Query("SELECT * FROM rocket_summaries WHERE launch_flight_number = :flightNumber")
    suspend fun getRocketForLaunch(flightNumber: Int): RocketSummary

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLaunch(launch: Launch)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLaunches(vararg launch: Launch)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLaunches(launches: List<Launch>)

    @Delete
    suspend fun deleteLaunch(launch: Launch)

}