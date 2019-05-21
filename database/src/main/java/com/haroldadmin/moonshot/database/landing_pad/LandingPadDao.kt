package com.haroldadmin.moonshot.database.landing_pad

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haroldadmin.moonshot.models.landingpad.LandingPad

@Dao
interface LandingPadDao {

    @Query("SELECT * FROM landing_pads")
    suspend fun getAllLandingPads(): List<LandingPad>

    @Query("SELECT * FROM landing_pads WHERE id = :id")
    suspend fun getLandingPad(id: String): LandingPad

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLandingPad(landingPad: LandingPad)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLandingPads(vararg landingPad: LandingPad)

    @Delete
    suspend fun deleteLandingPad(landingPad: LandingPad)
}