package com.haroldadmin.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path

enum class CoreStatus {
    active, inactive, unknown, expended, lost, retired
}

@JsonClass(generateAdapter = true)
data class Core(
    @Json(name = "id") val id: String,
    @Json(name = "serial") val serial: String,
    @Json(name = "block") val block: Int?,
    @Json(name = "status") val status: CoreStatus,
    @Json(name = "reuse_count") val reuseCount: Int?,
    @Json(name = "rtls_attempts") val rtlsAttempts: Int?,
    @Json(name = "rtls_landings") val rtlsLandings: Int?,
    @Json(name = "asds_attempts") val asdsAttempts: Int?,
    @Json(name = "asds_landings") val asdsLandings: Int?,
    @Json(name = "last_update") val lastUpdate: String?,
    @Json(name = "launches") val launchIDs: List<String>
)

interface CoreService {
    @GET("cores/{id}")
    suspend fun one(
        @Path("id") id: String
    ): NetworkResponse<Core, String>

    @GET("cores")
    suspend fun all(): NetworkResponse<List<Core>, String>
}