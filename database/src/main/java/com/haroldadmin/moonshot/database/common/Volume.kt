package com.haroldadmin.moonshot.database.common

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "volumes")
data class Volume(
    @ColumnInfo(name = "cubic_meters")
    val cubicMeters: Double,
    @ColumnInfo(name = "cubic_feet")
    val cubicFeet: Double,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "volume_id")
    val volumeId: Int = -1
)