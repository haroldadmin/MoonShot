package com.haroldadmin.moonshot.models.launch.rocket.second_stage.payload

import java.util.*

data class OrbitParams(
    val referenceSystem: String?,
    val regime: String?,
    val longitude: Double?,
    val semiMajorAxisKm: Double?,
    val eccentricity: Double?,
    val periapsisKm: Double?,
    val apoapsis: Double?,
    val inclinationDeg: Double?,
    val periodMin: Double?,
    val lifespanYears: Double?,
    val epoch: Date?,
    val meanMotion: Double?,
    val raan: Double?,
    val argOfPericenter: Double?,
    val meanAnomaly: Double?
) {

    companion object {
        fun getSampleOrbitParams(): OrbitParams {
            return OrbitParams(
                referenceSystem = null,
                regime = null,
                lifespanYears = null,
                semiMajorAxisKm = null,
                eccentricity = null,
                periapsisKm = null,
                apoapsis = null,
                inclinationDeg = null,
                periodMin = null,
                epoch = null,
                meanMotion = null,
                raan = null,
                argOfPericenter = null,
                meanAnomaly = null,
                longitude = null
            )
        }
    }
}