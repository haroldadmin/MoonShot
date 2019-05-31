package com.haroldadmin.moonshot.database.landingPad

import androidx.room.Dao
import androidx.room.Query
import com.haroldadmin.moonshot.database.BaseDao
import com.haroldadmin.moonshot.models.landingpad.LandingPad

@Dao
abstract class LandingPadDao : BaseDao<LandingPad> {

    @Query("SELECT * FROM landing_pads")
    abstract suspend fun getAllLandingPads(): List<LandingPad>

    @Query("SELECT * FROM landing_pads WHERE id = :id")
    abstract suspend fun getLandingPad(id: String): LandingPad
}