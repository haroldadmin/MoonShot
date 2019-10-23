package com.haroldadmin.moonshot.database.launch

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.haroldadmin.moonshot.database.BaseDao
import com.haroldadmin.moonshot.database.Projection
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.launch.LaunchPictures
import com.haroldadmin.moonshot.models.launch.LaunchStats
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.CoreSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.FirstStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.SecondStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.payload.Payload

const val LAUNCH_MINIMAL_PROJECTION: Projection =
    """flight_number, mission_name, missionPatchSmall, launch_date_utc, launch_success, details, siteName, siteNameLong, siteId, youtubeKey, redditCampaign, redditLaunch, redditMedia, wikipedia, tbd, is_tentative, tentative_max_precision"""

const val LAUNCH_STATS_PROJECTION: Projection =
    """rocket_summaries.rocket_name, rocket_summaries.rocket_type, rocket_summaries.rocket_id, COUNT(core_summaries.core_serial) as core_count, COUNT(payloads.payload_id) as payload_count"""

@Dao
abstract class LaunchDao : BaseDao<Launch> {

    @Query("""
        SELECT $LAUNCH_MINIMAL_PROJECTION
        FROM launches 
        WHERE launch_date_utc >= :timestamp
        ORDER BY launch_date_utc ASC
        LIMIT :limit
        """)
    abstract suspend fun getUpcomingLaunches(
        timestamp: Long,
        limit: Int
    ): List<LaunchMinimal>

    @Query(
        """
        SELECT $LAUNCH_MINIMAL_PROJECTION
        FROM launches 
        WHERE launch_date_utc <= :timestamp 
        ORDER BY launch_date_utc DESC
        LIMIT :limit
        """
    )
    abstract suspend fun getPastLaunches(timestamp: Long, limit: Int): List<LaunchMinimal>

    @Query(
        """
        SELECT $LAUNCH_MINIMAL_PROJECTION
        FROM launches
        ORDER BY launch_date_utc DESC
        LIMIT :limit
        """
    )
    abstract suspend fun getAllLaunches(
        limit: Int
    ): List<LaunchMinimal>

    @Query(
        """
        SELECT $LAUNCH_MINIMAL_PROJECTION
        FROM launches
        WHERE upcoming = 1 
        ORDER BY flight_number ASC
        LIMIT 1
        """
    )
    abstract suspend fun getNextLaunch(): LaunchMinimal?

    @Query("""
        SELECT *
        FROM launches
        WHERE upcoming = 1
        ORDER BY flight_number ASC
        LIMIT 1
    """)
    abstract suspend fun getNextFullLaunch(): Launch?

    @Query(
        """
        SELECT $LAUNCH_STATS_PROJECTION
        FROM rocket_summaries
        INNER JOIN core_summaries ON rocket_summaries.launch_flight_number = core_summaries.launch_flight_number
        INNER JOIN payloads ON rocket_summaries.launch_flight_number = payloads.launch_flight_number
        WHERE rocket_summaries.launch_flight_number = :flightNumber
        """
    )
    abstract suspend fun getLaunchStats(flightNumber: Int): LaunchStats?

    @Query(
        """
        SELECT $LAUNCH_MINIMAL_PROJECTION
        FROM launches
        WHERE flight_number = :flightNumber
        """
    )
    abstract suspend fun getLaunch(flightNumber: Int): LaunchMinimal?

    @Query(
        """
        SELECT flickrImages
        FROM launches
        WHERE flight_number = :flightNumber
        """
    )
    abstract suspend fun getLaunchPictures(flightNumber: Int): LaunchPictures?

    @Query(
        """
        SELECT $LAUNCH_MINIMAL_PROJECTION
        FROM launches
        WHERE siteId = :siteId
        ORDER BY launch_date_utc DESC
        LIMIT :limit
        """
    )
    abstract suspend fun getAllLaunchesForLaunchPad(
        siteId: String,
        limit: Int
    ): List<LaunchMinimal>

    @Query(
        """
        SELECT $LAUNCH_MINIMAL_PROJECTION
        FROM launches
        WHERE siteId = :siteId
        AND launch_date_utc <= :maxTimeStamp
        ORDER BY launch_date_utc DESC
        LIMIT :limit
        """
    )
    abstract suspend fun getPastLaunchesForLaunchPad(
        siteId: String,
        maxTimeStamp: Long,
        limit: Int
    ): List<LaunchMinimal>

    @Query(
        """
        SELECT $LAUNCH_MINIMAL_PROJECTION
        FROM launches
        WHERE siteId = :siteId
        AND launch_date_utc >= :minTimeStamp
        ORDER BY launch_date_utc ASC
        LIMIT :limit
    """
    )
    abstract suspend fun getUpcomingLaunchesForLaunchPad(
        siteId: String,
        minTimeStamp: Long,
        limit: Int
    ): List<LaunchMinimal>

    @Query(
        """
            SELECT $LAUNCH_MINIMAL_PROJECTION
            FROM launches
            WHERE launch_date_utc <= :end AND launch_date_utc >= :start
            ORDER BY launch_date_utc ASC
            LIMIT :limit
        """
    )
    abstract suspend fun getLaunchesInRange(start: Long, end: Long, limit: Int): List<LaunchMinimal>

    @Query(
        """
            SELECT $LAUNCH_MINIMAL_PROJECTION
            FROM launches
            WHERE mission_name LIKE :query
            ORDER BY mission_name
            LIMIT :limit
        """
    )
    abstract suspend fun getLaunchesForQuery(query: String, limit: Int): List<LaunchMinimal>

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveLaunch(launches: Launch)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveLaunches(launches: List<Launch>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveRocketSummary(rocketSummary: RocketSummary)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveRocketSummaries(rocketSummaries: List<RocketSummary>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveFirstStageSummary(firstStageSummary: FirstStageSummary)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveFirstStageSummaries(firstStageSummaries: List<FirstStageSummary>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveSecondStageSummary(secondStageSummary: SecondStageSummary)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveSecondStageSummaries(secondStageSummaries: List<SecondStageSummary>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveCoreSummaries(coreSummaries: List<CoreSummary>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun savePayloads(payloads: List<Payload>)
}