package com.haroldadmin.spacex_api_wrapper.rocket

import com.haroldadmin.spacex_api_wrapper.common.Mass
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import sun.security.util.Length

@JsonClass(generateAdapter = true)
data class Rocket (
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "active") val active: Boolean,
    @field:Json(name = "stages") val stages: Int,
    @field:Json(name = "boosters") val boosters: Int,
    @field:Json(name = "cost_per_launch") val costPerLaunch: Long,
    @field:Json(name = "success_rate_pct") val successRate: Double,
    @field:Json(name = "first_flight") val firstFlight: String,
    @field:Json(name = "country") val country: String,
    @field:Json(name = "company") val company: String,
    @field:Json(name = "height") val height: Length,
    @field:Json(name = "diameter") val diameter: Length,
    @field:Json(name = "mass") val mass: Mass,
    @field:Json(name = "payload_weights") val payloadWeights: List<PayloadWeight>,
    @field:Json(name = "first_stage") val firstStage: FirstStage,
    @field:Json(name = "second_stage") val secondStage: SecondStage,
    @field:Json(name = "engines") val engines: Engines,
    @field:Json(name = "landing_legs") val landingLegs: LandingLegs,
    @field:Json(name = "wikipedia") val wikipedia: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "rocket_id") val rockedId: String,
    @field:Json(name = "rocket_name") val rocketName: String,
    @field:Json(name = "rocket_type") val rocketType: String
)
