package com.haroldadmin.moonshot.database.rocket

import androidx.room.Dao
import androidx.room.Query
import com.haroldadmin.moonshot.database.BaseDao
import com.haroldadmin.moonshot.models.rocket.PayloadWeight

@Dao
abstract class PayloadWeightsDao : BaseDao<PayloadWeight> {

    @Query("SELECT * FROM payload_weights")
    abstract suspend fun getAllPayloadWeights(): List<PayloadWeight>

    @Query("SELECT * FROM payload_weights WHERE id = :id")
    abstract suspend fun getPayloadWeight(id: Int): PayloadWeight
}