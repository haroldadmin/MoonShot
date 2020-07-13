package com.haroldadmin.moonshot.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET

@JsonClass(generateAdapter = true)
data class CompanyInfo(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String?,
    @Json(name = "founder") val founder: String?,
    @Json(name = "founded") val founded: Int?,
    @Json(name = "employees") val employees: Int?,
    @Json(name = "vehicles") val vehicles: Int?,
    @Json(name = "launch_sites") val launchSites: Int?,
    @Json(name = "test_sites") val testSites: Int?,
    @Json(name = "ceo") val ceo: String?,
    @Json(name = "coo") val coo: String?,
    @Json(name = "cto") val cto: String?,
    @Json(name = "cto_propulsion") val ctoPropulsion: String?,
    @Json(name = "valuation") val valuation: Double?,
    @Json(name = "headquarters") val headquarters: Headquarters?,
    @Json(name = "links") val links: Links?,
    @Json(name = "summary") val summary: String?
) {

    @JsonClass(generateAdapter = true)
    data class Headquarters(
        @Json(name = "address") val address: String?,
        @Json(name = "city") val city: String?,
        @Json(name = "state") val state: String?
    )

    @JsonClass(generateAdapter = true)
    data class Links(
        @Json(name = "website") val website: String?,
        @Json(name = "flickr") val flickr: String?,
        @Json(name = "twitter") val twitter: String?,
        @Json(name = "elon_twitter") val elonTwitter: String?
    )
}

interface CompanyInfoService {

    @GET("company")
    suspend fun info(): NetworkResponse<CompanyInfo, String>
}