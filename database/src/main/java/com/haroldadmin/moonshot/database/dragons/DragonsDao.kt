package com.haroldadmin.moonshot.database.dragons

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haroldadmin.moonshot.models.dragon.Dragon
import com.haroldadmin.moonshot.models.dragon.DragonWithThrusters

@Dao
interface DragonsDao {

    @Query("SELECT * FROM dragons WHERE dragon_id = :id")
    suspend fun getDragon(id: String): Dragon

    @Query("SELECT * FROM dragons WHERE dragon_id = :id")
    suspend fun getDragonWithThrusters(id: String): DragonWithThrusters

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDragon(dragon: Dragon)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDragons(vararg dragon: Dragon)

    @Delete
    suspend fun deleteDragon(dragon: Dragon)

}