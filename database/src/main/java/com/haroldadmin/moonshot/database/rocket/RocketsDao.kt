package com.haroldadmin.moonshot.database.rocket

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.haroldadmin.moonshot.database.BaseDao
import com.haroldadmin.moonshot.database.Projection
import com.haroldadmin.moonshot.database.launch.LAUNCH_MINIMAL_PROJECTION
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.rocket.PayloadWeight
import com.haroldadmin.moonshot.models.rocket.Rocket
import com.haroldadmin.moonshot.models.rocket.RocketMinimal
import com.haroldadmin.moonshot.models.rocket.RocketWithPayloadWeights

private const val ROCKET_MINIMAL_PROJECTION: Projection =
    """rocket_id, rocket_name, rocket_type, active, cost_per_launch, success_rate, description"""

@Dao
abstract class RocketsDao : BaseDao<Rocket> {

    @Query("SELECT * FROM rockets WHERE rocket_id = :rocketId")
    @Transaction
    abstract suspend fun getRocketWithPayloadWeights(rocketId: String): RocketWithPayloadWeights

    @Query("""
        SELECT $ROCKET_MINIMAL_PROJECTION
        FROM rockets
        ORDER BY rocket_name
        DESC
    """)
    abstract suspend fun getAllRockets(): List<RocketMinimal>

    @Query("""
        SELECT $ROCKET_MINIMAL_PROJECTION
        FROM rockets
        WHERE rocket_id = :rocketId
    """)
    abstract suspend fun getRocket(rocketId: String): RocketMinimal?

    @Query("""
        SELECT $LAUNCH_MINIMAL_PROJECTION
        FROM launches
        WHERE flight_number IN
        (SELECT launch_flight_number
        FROM rocket_summaries
        WHERE rocket_id = :rocketId) AND launch_date_utc <= :currentTime
        ORDER BY launch_date_utc DESC
        LIMIT :limit
    """)
    abstract suspend fun getLaunchesForRocket(rocketId: String, currentTime: Long, limit: Int): List<LaunchMinimal>

    @Query(
        """
            SELECT $ROCKET_MINIMAL_PROJECTION
            FROM rockets
            WHERE rocket_name LIKE :query
            ORDER BY rocket_name
            LIMIT :limit
        """
    )
    abstract suspend fun getRocketsForQuery(query: String, limit: Int): List<RocketMinimal>

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