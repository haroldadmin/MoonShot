package com.haroldadmin.services.spacex.v4

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Rocket(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String?,
    @Json(name = "type") val type: String?,
    @Json(name = "active") val active: Boolean?,
    @Json(name = "stages") val stages: Int?,
    @Json(name = "boosters") val boosters: Int?,
    @Json(name = "cost_per_launch") val costPerLaunch: Double?,
    @Json(name = "success_rate_pct") val successRatePercentage: Double?,
    @Json(name = "first_flight") val firstFlight: String?,
    @Json(name = "country") val country: String?,
    @Json(name = "company") val company: String?,
    @Json(name = "height") val height: Length?,
    @Json(name = "diameter") val diameter: Length?,
    @Json(name = "mass") val mass: Mass?,
    @Json(name = "payload_weights") val payloadWeights: List<PayloadWeight>,
    @Json(name = "first_stage") val firstStage: FirstStage?,
    @Json(name = "second_stage") val secondStage: SecondStage?,
    @Json(name = "engines") val engines: Engines?,
    @Json(name = "landing_legs") val landingLegs: LandingLegs?,
    @Json(name = "flickr_images") val flickrImages: List<String>,
    @Json(name = "wikipedia") val wikipedia: String?,
    @Json(name = "description") val description: String?
) {

    @JsonClass(generateAdapter = true)
    data class Length(
        @Json(name = "meters") val metres: Double?,
        @Json(name = "feet") val feet: Double?
    )

    @JsonClass(generateAdapter = true)
    data class Mass(
        @Json(name = "kg") val kg: Double?,
        @Json(name = "lb") val lb: Double?
    )

    @JsonClass(generateAdapter = true)
    data class PayloadWeight(
        @Json(name = "id") val id: String?,
        @Json(name = "name") val name: String?,
        @Json(name = "kg") val kg: Double?,
        @Json(name = "lb") val lb: Double?
    )

    @JsonClass(generateAdapter = true)
    data class FirstStage(
        @Json(name = "reusable") val reusable: Boolean?,
        @Json(name = "engines") val engines: Int?,
        @Json(name = "fuel_amount_tons") val fuelAmountTons: Double?,
        @Json(name = "burn_time_sec") val burnTimeSec: Double?,
        @Json(name = "thrust_sea_level") val thrustSeaLevel: Thrust?,
        @Json(name = "thrust_vacuum") val thrustVacuum: Thrust?
    )

    @JsonClass(generateAdapter = true)
    data class Thrust(
        @Json(name = "kN") val kN: Double?,
        @Json(name = "lbf") val lbf: Double?
    )

    @JsonClass(generateAdapter = true)
    data class SecondStage(
        @Json(name = "reusable") val reusable: Boolean?,
        @Json(name = "engines") val engines: Int?,
        @Json(name = "fuel_amount_tons") val fuelAmountTons: Double?,
        @Json(name = "burn_time_sec") val burnTimeSec: Double?,
        @Json(name = "thrust") val thrust: Thrust?,
        @Json(name = "payloads") val payloads: Payloads?
    ) {

        @JsonClass(generateAdapter = true)
        data class Payloads(
            @Json(name = "option_1") val optionOne: String?,
            @Json(name = "composite_fairing") val compositeFairing: CompositeFairing?
        ) {

            @JsonClass(generateAdapter = true)
            data class CompositeFairing(
                @Json(name = "height") val height: Length,
                @Json(name = "diameter") val diameter: Length
            )
        }
    }

    @JsonClass(generateAdapter = true)
    data class Engines(
        @Json(name = "number") val number: Int?,
        @Json(name = "type") val type: String?,
        @Json(name = "version") val version: String?,
        @Json(name = "layout") val layout: String?,
        @Json(name = "isp") val isp: ISP?,
        @Json(name = "engine_loss_max") val engineLossMax: Int?,
        @Json(name = "propellant_1") val propellantOne: String?,
        @Json(name = "propellant_2") val propellantTwo: String?,
        @Json(name = "thrust_sea_level") val thrustSeaLevel: Thrust?,
        @Json(name = "thrust_vacuum") val thrustVacuum: Thrust?,
        @Json(name = "thrust_to_weight") val thrustToWeight: Double?
    ) {

        @JsonClass(generateAdapter = true)
        data class ISP(
            @Json(name = "sea_level") val seaLevel: Double?,
            @Json(name = "vacuum") val vacuum: Double?
        )
    }

    @JsonClass(generateAdapter = true)
    data class LandingLegs(
        @Json(name = "number") val number: Int?,
        @Json(name = "material") val material: String?
    )
}
