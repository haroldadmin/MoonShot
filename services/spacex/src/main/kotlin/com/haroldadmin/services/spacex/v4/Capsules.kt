package com.haroldadmin.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path

enum class CapsuleStatus {
    unknown, active, retired, destroyed
}

@JsonClass(generateAdapter = true)
data class Capsule(
    @Json(name="id") val id: String,
    @Json(name="serial") val serial: String,
    @Json(name="status") val status: CapsuleStatus,
    @Json(name="dragon") val dragon: String?,
    @Json(name="reuse_count") val reuseCount: Int?,
    @Json(name="water_landings") val waterLandings: Int?,
    @Json(name="land_landings") val landLandings: Int?,
    @Json(name="last_update") val lastUpdate: String?,
    @Json(name="launches") val launchIDs: List<String>
)

interface CapsuleService {
    @GET("capsules")
    suspend fun all(): NetworkResponse<List<Capsule>, String>

    @GET("capsules/{id}")
    suspend fun one(
        @Path("id") id: String
    ): NetworkResponse<Capsule, String>
}
