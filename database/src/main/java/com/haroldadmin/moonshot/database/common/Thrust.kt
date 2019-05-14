package com.haroldadmin.moonshot.database.common

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "thrusts")
data class Thrust(
    @ColumnInfo(name = "kN")
    val kN: Double,
    @ColumnInfo(name = "lbf")
    val lbf: Double
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "thrust_id")
    var thrustId: Int? = null
}