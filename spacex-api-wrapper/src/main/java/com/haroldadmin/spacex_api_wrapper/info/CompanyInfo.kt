package com.haroldadmin.spacex_api_wrapper.info

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompanyInfo (
    @field:Json(name = "name") val name: String,
    @field:Json(name = "founder") val founder: String,
    @field:Json(name = "founded") val foundedYear: Int,
    @field:Json(name = "employees") val employees: Int,
    @field:Json(name = "vehicles") val vehicles: Int,
    @field:Json(name = "launch_sites") val launchSites: Int,
    @field:Json(name = "test_sites") val testSites: Int,
    @field:Json(name = "ceo") val ceo: String,
    @field:Json(name = "cto") val cto: String,
    @field:Json(name = "coo") val coo: String,
    @field:Json(name = "cto_propulsion") val ctoPropulsion: String,
    @field:Json(name = "valuation") val valuation: Long,
    @field:Json(name = "headquarters") val headquarters: Headquarters,
    @field:Json(name = "summary") val summary: String
)