package com.haroldadmin.moonshot.database.rocket

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.haroldadmin.moonshot.database.BaseDao
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.rocket.PayloadWeight
import com.haroldadmin.moonshot.models.rocket.Rocket
import com.haroldadmin.moonshot.models.rocket.RocketMinimal
import com.haroldadmin.moonshot.models.rocket.RocketWithPayloadWeights

@Dao
abstract class RocketsDao : BaseDao<Rocket> {

    @Query("SELECT * FROM rockets")
    abstract suspend fun getAllRockets(): List<Rocket>

    @Query("SELECT * FROM rockets WHERE rocket_id = :rocketId")
    abstract suspend fun getRocket(rocketId: String): Rocket

    @Query("SELECT * FROM rockets WHERE rocket_id = :rocketId")
    @Transaction
    abstract suspend fun getRocketWithPayloadWeights(rocketId: String): RocketWithPayloadWeights

    @Query("""
        SELECT rocket_id, rocket_name, rocket_type, active, cost_per_launch, success_rate, description
        FROM rockets
        ORDER BY rocket_name
        DESC
    """)
    abstract suspend fun getAllRocketsMinimal(): List<RocketMinimal>

    @Query("""
        SELECT rocket_id, rocket_name, rocket_type, active, cost_per_launch, success_rate, description
        FROM rockets
        WHERE rocket_id = :rocketId
    """)
    abstract suspend fun getRocketMinimal(rocketId: String): RocketMinimal?

    @Query("""
        SELECT flight_number, mission_name, missionPatchSmall, launch_date_utc, details, siteName, siteNameLong, siteId, youtubeKey, redditCampaign, redditLaunch, redditMedia, wikipedia
        FROM launches
        WHERE flight_number IN
        (SELECT launch_flight_number
        FROM rocket_summaries
        WHERE rocket_id = :rocketId) AND launch_date_utc <= :timestamp
        ORDER BY launch_date_utc DESC
        LIMIT 10
    """)
    abstract suspend fun getLaunchesForRocket(rocketId: String, timestamp: Long): List<LaunchMinimal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun savePayloadWeights(payloadWeights: List<PayloadWeight>)

    @Transaction
    open suspend fun saveRocketsWithPayloadWeights(rockets: List<Rocket>, payloadWeights: List<PayloadWeight>) {
        saveAll(rockets)
        savePayloadWeights(payloadWeights)
    }

    @Transaction
    open suspend fun saveRocketWithPayloadWeights(rocket: Rocket, payloadWeights: List<PayloadWeight>) {
        save(rocket)
        savePayloadWeights(payloadWeights)
    }

    @Delete
    abstract suspend fun deleteRocket(rocket: Rocket)
}