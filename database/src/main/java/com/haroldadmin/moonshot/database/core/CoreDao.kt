package com.haroldadmin.moonshot.database.core

import androidx.room.Dao
import androidx.room.Insert
import io.reactivex.Completable

@Dao
interface CoreDao {

    @Insert
    fun saveCore(core: Core): Completable

    @Insert
    fun saveCores(vararg cores: Core): Completable

}