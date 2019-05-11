package com.haroldadmin.spacex_api_wrapper.rocket

import com.haroldadmin.spacex_api_wrapper.common.Mass
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import sun.security.util.Length

@JsonClass(generateAdapter = true)
data class Rocket (
    @Json(name = "id") val id: Int,
    @Json(name = "active") val active: Boolean,
    @Json(name = "stages") val stages: Int,
    @Json(name = "boosters") val boosters: Int,
    @Json(name = "cost_per_launch") val costPerLaunch: Long,
    @Json(name = "success_rate_pct") val successRate: Double,
    @Json(name = "first_flight") val firstFlight: String,
    @Json(name = "country") val country: String,
    @Json(name = "company") val company: String,
    @Json(name = "height") val height: Length,
    @Json(name = "diameter") val diameter: Length,
    @Json(name = "mass") val mass: Mass,
    @Json(name = "payload_weights") val payloadWeights: List<PayloadWeight>,
    @Json(name = "first_stage") val firstStage: FirstStage,
    @Json(name = "second_stage") val secondStage: SecondStage,
    @Json(name = "engines") val engines: Engines,
    @Json(name = "landing_legs") val landingLegs: LandingLegs,
    @Json(name = "wikipedia") val wikipedia: String,
    @Json(name = "description") val description: String,
    @Json(name = "rocket_id") val rockedId: String,
    @Json(name = "rocket_name") val rocketName: String,
    @Json(name = "rocket_type") val rocketType: String
)
