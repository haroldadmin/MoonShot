package com.haroldadmin.moonshot.database.launch

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.haroldadmin.moonshot.database.BaseDao
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.CoreSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.FirstStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.SecondStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.payload.Payload

@Dao
abstract class LaunchDao : BaseDao<Launch> {

    @Query("SELECT * FROM launches ORDER BY launch_date_utc DESC LIMIT :limit")
    abstract suspend fun getAllLaunches(limit: Int): List<Launch>

    @Query("SELECT * FROM launches WHERE launch_date_utc < :timestamp ORDER BY launch_date_utc DESC LIMIT :limit")
    abstract suspend fun getAllLaunches(timestamp: Long, limit: Int): List<Launch>

    @Query("SELECT * FROM launches WHERE flight_number = :flightNumber")
    abstract suspend fun getLaunch(flightNumber: Int): Launch?

    @Query("SELECT * FROM launches WHERE launch_date_utc >= :timestamp")
    abstract suspend fun getUpcomingLaunches(timestamp: Long): List<Launch>

    @Query("SELECT * FROM launches WHERE launch_date_utc >= :timestamp LIMIT 1")
    abstract suspend fun getNextLaunch(timestamp: Long): Launch?

    @Query("SELECT * FROM launches WHERE launch_date_utc < :timestamp")
    abstract suspend fun getPastLaunches(timestamp: Long): List<Launch>

    @Query("SELECT * FROM rocket_summaries WHERE launch_flight_number = :flightNumber")
    abstract suspend fun getRocketForLaunch(flightNumber: Int): RocketSummary?

    @Transaction
    open suspend fun saveLaunchWithSummaries(
        launch: Launch,
        rocketSummary: RocketSummary,
        firstStageSummary: FirstStageSummary,
        secondStageSummary: SecondStageSummary,
        coreSummaries: List<CoreSummary>,
        payloads: List<Payload>
    ) {
        saveLaunch(launch)
        saveRocketSummary(rocketSummary)
        saveFirstStageSummary(firstStageSummary)
        saveSecondStageSummary(secondStageSummary)
        saveCoreSummaries(coreSummaries)
        savePayloads(payloads)
    }

    @Transaction
    open suspend fun saveLaunchesWithSummaries(
        launches: List<Launch>,
        rocketSummaries: List<RocketSummary>,
        firstStageSummaries: List<FirstStageSummary>,
        coreSummaries: List<CoreSummary>,
        secondStageSummaries: List<SecondStageSummary>,
        payloads: List<Payload>
    ) {
        saveLaunches(launches)
        saveRocketSummaries(rocketSummaries)
        saveFirstStageSummaries(firstStageSummaries)
        saveCoreSummaries(coreSummaries)
        saveSecondStageSummaries(secondStageSummaries)
        savePayloads(payloads)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun saveLaunch(launches: Launch)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun saveLaunches(launches: List<Launch>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun saveRocketSummary(rocketSummary: RocketSummary)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun saveRocketSummaries(rocketSummaries: List<RocketSummary>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun saveFirstStageSummary(firstStageSummary: FirstStageSummary)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun saveFirstStageSummaries(firstStageSummaries: List<FirstStageSummary>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun saveSecondStageSummary(secondStageSummary: SecondStageSummary)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun saveSecondStageSummaries(secondStageSummaries: List<SecondStageSummary>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun saveCoreSummaries(coreSummaries: List<CoreSummary>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun savePayloads(payloads: List<Payload>)
}