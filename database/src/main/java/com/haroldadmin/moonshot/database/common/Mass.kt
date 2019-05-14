package com.haroldadmin.moonshot.database.common

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "masses")
data class Mass(
    @ColumnInfo(name = "kg")
    val kg: Double,
    @ColumnInfo(name = "lb")
    val lb: Double
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mass_id")
    var massId: Int = -1
}