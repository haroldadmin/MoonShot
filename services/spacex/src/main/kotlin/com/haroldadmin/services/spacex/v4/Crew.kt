package com.haroldadmin.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path

enum class CrewStatus {
    active, inactive, retired, unknown
}

@JsonClass(generateAdapter = true)
data class Crew(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String?,
    @Json(name = "status") val status: CrewStatus,
    @Json(name = "agency") val agency: String?,
    @Json(name = "image") val image: String?,
    @Json(name = "wikipedia") val wikipedia: String?,
    @Json(name = "launches") val launchIDs: List<String>
)

interface CrewService {

    @GET("crew/{id}")
    suspend fun one(
        @Path("id") id: String
    ): NetworkResponse<Crew, String>

    @GET("crew")
    suspend fun all(): NetworkResponse<List<Crew>, String>
}
