package com.haroldadmin.moonshot.models.common

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class Location(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "region")
    val region: String,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "location_id")
    var id: Int? = null

}