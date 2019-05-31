package com.haroldadmin.moonshot.database.dragons

import androidx.room.Dao
import androidx.room.Query
import com.haroldadmin.moonshot.database.BaseDao
import com.haroldadmin.moonshot.models.dragon.Thruster

@Dao
abstract class ThrustersDao : BaseDao<Thruster> {

    @Query("SELECT * FROM thrusters WHERE thruster_id = :id")
    abstract suspend fun getThruster(id: Int): Thruster

    @Query("SELECT * FROM thrusters")
    abstract suspend fun getAllThrusters(): List<Thruster>
}