package com.haroldadmin.moonshot.database.launch

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.haroldadmin.moonshot.database.launch.rocket.RocketSummary
import io.reactivex.Completable
import io.reactivex.Flowable
import java.util.*

@Dao
interface LaunchDao {

    @Query("SELECT * FROM launches")
    fun getAllLaunches(): Flowable<List<Launch>>

    @Query("SELECT * FROM launches WHERE flight_number = :flightNumber")
    fun getLaunch(flightNumber: Int): Flowable<Launch>

    @Query("SELECT * FROM launches WHERE launch_date_utc >= :timestamp")
    fun getUpcomingLaunches(timestamp: Long = Date().time.div(1000L)): Flowable<List<Launch>>

    @Query("SELECT * FROM launches WHERE launch_date_utc < :timestamp")
    fun getPastLaunches(timestamp: Long = Date().time.div(1000L)): Flowable<List<Launch>>

    @Query("SELECT * FROM rocket_summaries WHERE launch_flight_number = :flightNumber")
    fun getRocketForLaunch(flightNumber: Int): Flowable<RocketSummary>

    @Insert
    fun saveLaunch(launch: Launch): Completable

    @Insert
    fun saveLaunches(vararg launch: Launch): Completable

    @Delete
    fun deleteLaunch(launch: Launch): Completable

}