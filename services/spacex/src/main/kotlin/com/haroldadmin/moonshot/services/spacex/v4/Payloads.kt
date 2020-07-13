package com.haroldadmin.moonshot.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
data class Payload(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "type") val type: String?,
    @Json(name = "launch") val launchID: String?,
    @Json(name = "customers") val customers: List<String>,
    @Json(name = "norad_ids") val noradIDs: List<Int>,
    @Json(name = "nationalities") val nationalities: List<String>,
    @Json(name = "manufacturers") val manufacturers: List<String>,
    @Json(name = "mass_kg") val massKg: Double?,
    @Json(name = "mass_lbs") val massLbs: Double?,
    @Json(name = "orbit") val orbit: String?,
    @Json(name = "reference_system") val referenceSystem: String?,
    @Json(name = "regime") val regime: String?,
    @Json(name = "longitude") val longitude: Double?,
    @Json(name = "semi_major_axis_km") val semiMajorAxisKm: Double?,
    @Json(name = "eccentricity") val eccentricity: Double?,
    @Json(name = "periapsis_km") val periapsisKm: Double?,
    @Json(name = "apoapsis_km") val apoapsisKm: Double?,
    @Json(name = "inclination_deg") val inclinationDeg: Double?,
    @Json(name = "period_min") val periodMin: Double?,
    @Json(name = "lifespan_years") val lifespanYears: Double?,
    @Json(name = "epoch") val epoch: ZonedDateTime?,
    @Json(name = "mean_motion") val meanMotion: Double?,
    @Json(name = "raan") val raan: Double?,
    @Json(name = "arg_of_pericenter") val argOfPericentre: Double?,
    @Json(name = "mean_anomaly") val meanAnomaly: Double?,
    @Json(name = "dragon") val dragon: Dragon
) {
    @JsonClass(generateAdapter = true)
    data class Dragon(
        @Json(name = "capsule") val capsuleID: String?,
        @Json(name = "mass_returned_kg") val massReturnedKg: Double?,
        @Json(name = "mass_returned_lbs") val massReturnedLbs: Double?,
        @Json(name = "flight_time_sec") val flightTimeSec: Double?,
        @Json(name = "manifest") val manifest: String?,
        @Json(name = "water_landing") val waterLanding: Boolean?,
        @Json(name = "land_landing") val landLanding: Boolean?
    )
}

interface PayloadService {

    @GET("payloads")
    suspend fun all(): NetworkResponse<List<Payload>, String>

    @GET("payloads/{id}")
    suspend fun one(
        @Path("id") id: String
    ): NetworkResponse<Payload, String>

}
