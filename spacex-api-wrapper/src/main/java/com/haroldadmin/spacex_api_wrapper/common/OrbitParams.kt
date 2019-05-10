package com.haroldadmin.spacex_api_wrapper.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class OrbitParams(
    @field:Json(name = "reference_system") val referenceSystem: String,
    @field:Json(name = "regime") val regime: String,
    @field:Json(name = "longitude") val longitude: Double?,
    @field:Json(name = "semi_major_axis_km") val semiMajorAxisKm: Double?,
    @field:Json(name = "eccentricity") val eccentricity: Double?,
    @field:Json(name = "periapsis_km") val periapsisKm: Double,
    @field:Json(name = "apoapsis_km") val apoapsisKm: Double,
    @field:Json(name = "inclination_deg") val inclinationDeg: Double,
    @field:Json(name = "period_min") val periodMin: Double?,
    @field:Json(name = "lifespan_years") val lifespanYears: Int?,
    @field:Json(name = "epoch") val epoch: Date?,
    @field:Json(name = "mean_motion") val meanMotion: Double?,
    @field:Json(name = "raan") val raan: Double?,
    @field:Json(name = "arg_of_pericenter") val argOfPericenter: Double?,
    @field:Json(name = "mean_anomaly") val meanAnomaly: Double?
)