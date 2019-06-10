package com.haroldadmin.moonshot.database.launchPad

import androidx.room.Dao
import androidx.room.Query
import com.haroldadmin.moonshot.database.BaseDao
import com.haroldadmin.moonshot.models.launchpad.LaunchPad

@Dao
abstract class LaunchPadDao : BaseDao<LaunchPad> {

    @Query("SELECT * from launch_pads LIMIT :limit")
    abstract suspend fun getAllLaunchPads(limit: Int): List<LaunchPad>

    @Query("SELECT * FROM launch_pads WHERE site_id = :siteId")
    abstract suspend fun getLaunchPad(siteId: String): LaunchPad?
}