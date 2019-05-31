package com.haroldadmin.moonshot.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface BaseDao <T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(obj: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(vararg obj: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(objs: List<T>)

    @Update
    suspend fun update(obj: T)

    @Update
    suspend fun updateAll(vararg obj: T)

    @Update
    suspend fun updateAll(objs: List<T>)

    @Delete
    suspend fun delete(obj: T)

    @Delete
    suspend fun deleteAll(vararg obj: T)

    @Delete
    suspend fun deleteAll(objs: List<T>)
}