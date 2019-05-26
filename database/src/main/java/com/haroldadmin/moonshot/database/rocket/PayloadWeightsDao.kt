package com.haroldadmin.moonshot.database.rocket

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haroldadmin.moonshot.models.rocket.PayloadWeight

@Dao
interface PayloadWeightsDao {

    @Query("SELECT * FROM payload_weights")
    suspend fun getAllPayloadWeights(): List<PayloadWeight>

    @Query("SELECT * FROM payload_weights WHERE id = :id")
    suspend fun getPayloadWeight(id: Int): PayloadWeight

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePayloadWeight(payloadWeight: PayloadWeight)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePayloadWeights(vararg payloadWeight: PayloadWeight)

    @Delete
    suspend fun deletePayloadWeight(payloadWeight: PayloadWeight)
}