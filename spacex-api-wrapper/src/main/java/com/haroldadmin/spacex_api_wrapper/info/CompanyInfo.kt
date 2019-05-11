package com.haroldadmin.spacex_api_wrapper.info

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompanyInfo (
    @Json(name = "name") val name: String,
    @Json(name = "founder") val founder: String,
    @Json(name = "founded") val foundedYear: Int,
    @Json(name = "employees") val employees: Int,
    @Json(name = "vehicles") val vehicles: Int,
    @Json(name = "launch_sites") val launchSites: Int,
    @Json(name = "test_sites") val testSites: Int,
    @Json(name = "ceo") val ceo: String,
    @Json(name = "cto") val cto: String,
    @Json(name = "coo") val coo: String,
    @Json(name = "cto_propulsion") val ctoPropulsion: String,
    @Json(name = "valuation") val valuation: Long,
    @Json(name = "headquarters") val headquarters: Headquarters,
    @Json(name = "summary") val summary: String
)