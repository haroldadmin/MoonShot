package com.haroldadmin.moonshot.models.landingpad

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haroldadmin.moonshot.models.common.Location

@Entity(tableName = "landing_pads")
data class LandingPad(
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
) {

    companion object {
        fun getSampleLandingPad(): LandingPad {
            return LandingPad(
                id = "LZ-4",
                fullName = "Landing Zone 4",
                status = "active",
                location = Location(
                    name = "Vandenberg Air Force Base",
                    region = "California",
                    latitude = 34.632989,
                    longitude = -120.615167
                ),
                landingType = "RTLS",
                attemptedLandings = 1,
                successfulLandings = 1,
                wikipedia = "https://en.wikipedia.org/wiki/Vandenberg_AFB_Space_Launch_Complex_4#LZ-4_landing_history",
                details = "SpaceX's west coast landing pad. The pad is adjacent to SLC-4E, SpaceX's west coast launch site. The pad was under construction for about a year starting in 2016. After concerns with seal mating season, this pad was first used for the SAOCOM 1A mission in October 2018. Officially referred to as LZ-4 in FCC filings."
            )
        }
    }
}