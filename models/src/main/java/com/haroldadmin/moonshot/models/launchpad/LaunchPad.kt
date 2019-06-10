package com.haroldadmin.moonshot.models.launchpad

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.models.common.Location

@Entity(tableName = "launch_pads")
data class LaunchPad(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "status")
    val status: String,
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
    val siteNameLong: String,
    @Embedded
    val location: Location
) {
    companion object {
        fun getSampleLanchpad(): LaunchPad {
            return LaunchPad(
                id = 1,
                status = "retired",
                location = Location(
                    name = "Omelek Island",
                    region = "Marshall Islang",
                    longitude = 167.7431292,
                    latitude = 9.0477206
                ),
                vehiclesLanded = listOf("Falcon 1"),
                attemptedLaunches = 5,
                successfulLaunches = 2,
                wikipedia = "https://en.wikipedia.org/wiki/Omelek_Island",
                details = "SpaceX original launch site, where all of the Falcon 1 launches occured. " +
                        "Abandoned as SpaceX decided against upgrading the pad to support Falcon 9.\"",
                siteId = "kwajalein_atoll",
                siteNameLong = "Kwajalein Atoll Omelek Island"
            )
        }
    }
}