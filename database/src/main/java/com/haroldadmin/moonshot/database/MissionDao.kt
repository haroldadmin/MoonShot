package com.haroldadmin.moonshot.database

import androidx.room.Dao
import androidx.room.Query
import com.haroldadmin.moonshot.models.Mission

@Dao
abstract class MissionDao: BaseDao<Mission> {

    @Query("""
        SELECT *
        FROM mission
        WHERE mission_id = :missionId
    """)
    abstract fun forId(missionId: String): Mission?

}