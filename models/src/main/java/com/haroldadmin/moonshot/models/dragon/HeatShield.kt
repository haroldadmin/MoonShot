package com.haroldadmin.moonshot.models.dragon

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class HeatShield(
    val material: String,
    val sizeMeters: Double,
    val tempDegrees: Double,
    val devPartner: String
)

