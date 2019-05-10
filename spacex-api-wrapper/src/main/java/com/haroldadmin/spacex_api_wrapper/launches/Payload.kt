package com.haroldadmin.spacex_api_wrapper.launches

import com.haroldadmin.spacex_api_wrapper.common.OrbitParams
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Payload(
    @field:Json(name = "payload_id") val id: String,
    @field:Json(name = "norad_id") val noradId: List<Int>,
    @field:Json(name = "reused") val reused: Boolean,
    @field:Json(name = "customers") val customers: List<String>,
    @field:Json(name = "nationality") val nationality: String,
    @field:Json(name = "manufacturer") val manufacturer: String,
    @field:Json(name = "payload_type") val payloadType: String,
    @field:Json(name = "payload_mass_kg") val payloadMassKg: Int,
    @field:Json(name = "payload_mass_lbs") val payloadMassLbs: Int,
    @field:Json(name = "orbit") val orbit: String,
    @field:Json(name = "orbit_params") val orbitParams: OrbitParams
)