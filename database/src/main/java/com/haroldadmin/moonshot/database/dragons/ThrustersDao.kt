package com.haroldadmin.moonshot.database.dragons

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haroldadmin.moonshot.models.dragon.Thruster

@Dao
interface ThrustersDao {

    @Query("SELECT * FROM thrusters WHERE thruster_id = :id")
    suspend fun getThruster(id: Int): Thruster

    @Query("SELECT * FROM thrusters")
    suspend fun getAllThrusters(): List<Thruster>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveThruster(thruster: Thruster)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveThrusters(vararg thruster: Thruster)

    @Delete
    suspend fun deleteThruster(thruster: Thruster)
}