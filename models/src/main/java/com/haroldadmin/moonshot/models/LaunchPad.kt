package com.haroldadmin.moonshot.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.models.common.Location

@Entity
data class LaunchPad(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "status")
    val status: String,
    @Embedded
    val location: Location,
    @ColumnInfo(name = "vehicles_landed")
    val vehiclesLanded: List<String>,
    @ColumnInfo(name = "attempted_launches")
    val attemptedLaunches: Int,
    @ColumnInfo(name = "succesfulLaunches")
    val successfulLaunches: Int,
    @ColumnInfo(name = "wikipedia")
    val wikipedia: String,
    @ColumnInfo(name = "details")
    val details: String,
    @ColumnInfo(name = "site_id")
    val siteId: String,
    @ColumnInfo(name = "site_name_long")
    val siteNameLong: String
)

fun LaunchPad.successPercentage(): String {
    return "$successfulLaunches/$attemptedLaunches"
}