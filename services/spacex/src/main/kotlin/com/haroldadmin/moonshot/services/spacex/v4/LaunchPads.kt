package com.haroldadmin.moonshot.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path

@JsonClass(generateAdapter = true)
data class LaunchPad (
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String?,
    @Json(name = "full_name") val fullName: String?,
    @Json(name = "status") val status: String,
    @Json(name = "locality") val locality: String?,
    @Json(name = "region") val region: String?,
    @Json(name = "timezone") val timezone: String?,
    @Json(name = "latitude") val latitude: Double?,
    @Json(name = "longitude") val longitude: Double?,
    @Json(name = "launch_attempts") val launchAttempts: Int?,
    @Json(name = "launch_successes") val launchSuccesses: Int?,
    @Json(name = "rockets") val rocketIDs: List<String>,
    @Json(name = "launches") val launchIDs: List<String>
)

interface LaunchPadsService {

    @GET("launchpads")
    suspend fun all(): NetworkResponse<List<LaunchPad>, String>

    @GET("launchpads/{id}")
    suspend fun one(
        @Path("id") id: String
    ): NetworkResponse<LaunchPad, String>
}
