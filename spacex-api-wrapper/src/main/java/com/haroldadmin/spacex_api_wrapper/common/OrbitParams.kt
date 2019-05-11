package com.haroldadmin.spacex_api_wrapper.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class OrbitParams(
    @Json(name = "reference_system") val referenceSystem: String,
    @Json(name = "regime") val regime: String,
    @Json(name = "longitude") val longitude: Double?,
    @Json(name = "semi_major_axis_km") val semiMajorAxisKm: Double?,
    @Json(name = "eccentricity") val eccentricity: Double?,
    @Json(name = "periapsis_km") val periapsisKm: Double,
    @Json(name = "apoapsis_km") val apoapsisKm: Double,
    @Json(name = "inclination_deg") val inclinationDeg: Double,
    @Json(name = "period_min") val periodMin: Double?,
    @Json(name = "lifespan_years") val lifespanYears: Int?,
    @Json(name = "epoch") val epoch: Date?,
    @Json(name = "mean_motion") val meanMotion: Double?,
    @Json(name = "raan") val raan: Double?,
    @Json(name = "arg_of_pericenter") val argOfPericenter: Double?,
    @Json(name = "mean_anomaly") val meanAnomaly: Double?
)