package com.haroldadmin.moonshot.database.dragons

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.haroldadmin.moonshot.database.BaseDao
import com.haroldadmin.moonshot.models.dragon.Dragon
import com.haroldadmin.moonshot.models.dragon.DragonWithThrusters

@Dao
abstract class DragonsDao : BaseDao<Dragon> {

    @Query("SELECT * FROM dragons WHERE dragon_id = :id")
    abstract suspend fun getDragon(id: String): Dragon

    @Query("SELECT * FROM dragons WHERE dragon_id = :id")
    @Transaction
    abstract suspend fun getDragonWithThrusters(id: String): DragonWithThrusters
}