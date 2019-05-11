package com.haroldadmin.moonshot.database.dragon

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "heatshields")
data class HeatShield(
    @ColumnInfo(name = "material")
    val material: String,
    @ColumnInfo(name = "size_meters")
    val sizeMeters: Int,
    @ColumnInfo(name = "temp_degrees")
    val tempDegrees: Int,
    @ColumnInfo(name = "dev_partner")
    val devPartner: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "heat_shield_id")
    val heatShieldId: Int = -1
)