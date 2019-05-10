package com.haroldadmin.spacex_api_wrapper.dragon

import com.haroldadmin.spacex_api_wrapper.common.Length
import com.haroldadmin.spacex_api_wrapper.common.Mass
import com.haroldadmin.spacex_api_wrapper.common.Volume
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class Dragon (
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name:  String,
    @field:Json(name = "type") val type: String,
    @field:Json(name = "active") val active: Boolean,
    @field:Json(name = "crew_capacity") val crewCapacity: Int,
    @field:Json(name = "sidewall_angle_deg") val sideWallAngleDeg: Int,
    @field:Json(name = "orbit_duration_yr") val orbitDurationYear: Int,
    @field:Json(name = "dry_mass_kg") val dryMassKg: Int,
    @field:Json(name = "dry_mass_lb") val dryMassLb: Int,
    @field:Json(name = "first_flight") val firstFlight: Date,
    @field:Json(name = "heat_shield") val heatShield: HeatShield,
    @field:Json(name = "thrusters") val thrusters: List<Thruster>,
    @field:Json(name = "launch_payload_mass") val launchPayloadMass: Mass,
    @field:Json(name = "launch_payload_vol") val launchPayloadVol: Volume,
    @field:Json(name = "return_payload_mass") val returnPayloadMass: Mass,
    @field:Json(name = "return_payload_vol") val returnPayloadVol: Volume,
    @field:Json(name = "pressurized_capsule") val pressurizedCapsule: PressurizedCapsule,
    @field:Json(name = "trunk") val trunk: Trunk,
    @field:Json(name = "height_w_trunk") val heightWTrunk: Length,
    @field:Json(name = "diameter") val diameter: Length,
    @field:Json(name = "wikipedia") val wikipdeia: String,
    @field:Json(name = "description") val description: String
)