package com.haroldadmin.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path

@JsonClass(generateAdapter = true)
data class Ship(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "model") val model: String?,
    @Json(name = "type") val type: String?,
    @Json(name = "roles") val roles: List<String>,
    @Json(name = "active") val isActive: Boolean,
    @Json(name = "imo") val imo: Double?,
    @Json(name = "mmsi") val mmsi: Double?,
    @Json(name = "abs") val abs: Double?,
    @Json(name = "class") val clazz: Double?,
    @Json(name = "mass_kg") val massKg: Double?,
    @Json(name = "mass_lbs") val massLbs: Double?,
    @Json(name = "year_built") val yearBuilt: Double?,
    @Json(name = "home_port") val homePort: String?,
    @Json(name = "status") val status: String?,
    @Json(name = "speed_kn") val speedKn: Double?,
    @Json(name = "course_deg") val courseDeg: Double?,
    @Json(name = "latitude") val latitude: Double?,
    @Json(name = "longitude") val longitude: Double?,
    @Json(name = "last_ais_update") val lastAisUpdate: String?,
    @Json(name = "link") val link: String?,
    @Json(name = "image") val image: String?,
    @Json(name = "launches") val launchIDs: List<String>
)

interface ShipsService {

    @GET("ships")
    suspend fun all(): NetworkResponse<List<Ship>, String>

    @GET("ships/{id}")
    suspend fun one(
        @Path("id") id: String
    ): NetworkResponse<Ship, String>
}
