package com.haroldadmin.moonshot.models.landingpad

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.models.common.Location

@Entity(tableName = "landing_pads")
data class LandingPad (
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "status") val status: String,
    @Embedded val location: Location,
    @ColumnInfo(name = "landing_type") val landingType: String,
    @ColumnInfo(name = "attempted_landings") val attemptedLandings: Int,
    @ColumnInfo(name = "successful_landings") val successfulLandings: Int,
    @ColumnInfo(name = "wikipedia") val wikipedia: String,
    @ColumnInfo(name = "details") val details: String
)