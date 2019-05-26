package com.haroldadmin.moonshot.database.rocket

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.haroldadmin.moonshot.models.rocket.Rocket
import com.haroldadmin.moonshot.models.rocket.RocketWithPayloadWeights

@Dao
interface RocketsDao {

    @Query("SELECT * FROM rockets")
    suspend fun getAllRockets(): List<Rocket>

    @Query("SELECT * FROM rockets WHERE rocket_id = :rocketId")
    suspend fun getRocket(rocketId: String): Rocket

    @Query("SELECT * FROM rockets WHERE rocket_id = :rocketId")
    @Transaction
    suspend fun getRocketWithPayloadWeights(rocketId: String): RocketWithPayloadWeights

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRocket(rocket: Rocket)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRockets(vararg rocket: Rocket)

    @Delete
    suspend fun deleteRocket(rocket: Rocket)

}