package com.haroldadmin.moonshot.database.launch.rocket.second_stage.payload

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

data class OrbitParams(
    val referenceSystem: String,
    val regime: String,
    val longitude: Double?,
    val semiMajorAxisKm: Double?,
    val eccentricity: Double?,
    val periapsisKm: Double,
    val apoapsis: Double,
    val inclinationDeg: Double,
    val periodMin: Double?,
    val lifespanYears: Int?,
    val epoch: Date?,
    val meanMotion: Double?,
    val raan: Double?,
    val argOfPericenter: Double?,
    val meanAnomaly: Double?
)