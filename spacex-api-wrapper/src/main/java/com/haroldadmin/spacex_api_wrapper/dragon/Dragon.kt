package com.haroldadmin.spacex_api_wrapper.dragon

import com.haroldadmin.spacex_api_wrapper.common.Length
import com.haroldadmin.spacex_api_wrapper.common.Mass
import com.haroldadmin.spacex_api_wrapper.common.Volume
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Dragon(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "type") val type: String,
    @Json(name = "active") val active: Boolean,
    @Json(name = "crew_capacity") val crewCapacity: Int,
    @Json(name = "sidewall_angle_deg") val sideWallAngleDeg: Double,
    @Json(name = "orbit_duration_yr") val orbitDurationYear: Double,
    @Json(name = "dry_mass_kg") val dryMassKg: Double,
    @Json(name = "dry_mass_lb") val dryMassLb: Double,
    @Json(name = "first_flight") val firstFlight: String?,
    @Json(name = "heat_shield") val heatShield: HeatShield,
    @Json(name = "thrusters") val thrusters: List<Thruster>,
    @Json(name = "launch_payload_mass") val launchPayloadMass: Mass,
    @Json(name = "launch_payload_vol") val launchPayloadVol: Volume,
    @Json(name = "return_payload_mass") val returnPayloadMass: Mass,
    @Json(name = "return_payload_vol") val returnPayloadVol: Volume,
    @Json(name = "pressurized_capsule") val pressurizedCapsule: PressurizedCapsule,
    @Json(name = "trunk") val trunk: Trunk,
    @Json(name = "height_w_trunk") val heightWTrunk: Length,
    @Json(name = "diameter") val diameter: Length,
    @Json(name = "wikipedia") val wikipdeia: String,
    @Json(name = "description") val description: String
)