package com.haroldadmin.moonshot.database.core

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface CoreDao {

    @Insert
    suspend fun saveCore(core: Core)

    @Insert
    suspend fun saveCores(vararg cores: Core)

}