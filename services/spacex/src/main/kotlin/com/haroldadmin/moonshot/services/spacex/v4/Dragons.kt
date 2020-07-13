package com.haroldadmin.moonshot.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class Dragon(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "type") val type: String,
    @Json(name = "active") val active: Boolean,
    @Json(name = "crew_capacity") val crewCapacity: Int,
    @Json(name = "sidewall_angle_deg") val sidewallAngleDegrees: Double,
    @Json(name = "orbit_duration_yr") val orbitDurationYears: Double,
    @Json(name = "dry_mass_kg") val dryMassKg: Double,
    @Json(name = "dry_mass_lb") val dryMassLb: Double,
    @Json(name = "first_flight") val firstFlight: LocalDate?,
    @Json(name = "heat_shield") val heatShield: HeatShield?,
    @Json(name = "thrusters") val thrusters: List<Thruster>,
    @Json(name = "launch_payload_mass") val launchPayloadMass: Mass?,
    @Json(name = "launch_payload_vol") val launchPayloadVolume: Volume?,
    @Json(name = "return_payload_mass") val returnPayloadMass: Mass?,
    @Json(name = "return_payload_vol") val returnPayloadVolume: Volume?,
    @Json(name = "pressurized_capsule") val pressurizedCapsule: PressurizedCapsule?,
    @Json(name = "trunk") val trunk: Trunk?,
    @Json(name = "height_w_trunk") val heightWithTrunk: Length?,
    @Json(name = "diameter") val diameter: Length?,
    @Json(name = "flickr_images") val flickrImages: List<String>,
    @Json(name = "wikipedia") val wikipedia: String?,
    @Json(name = "description") val description: String?
) {

    @JsonClass(generateAdapter = true)
    data class HeatShield(
        @Json(name = "material") val material: String,
        @Json(name = "size_meters") val sizeMetre: Double,
        @Json(name = "temp_degrees") val tempDegree: Double?,
        @Json(name = "dev_partner") val devPartner: String?
    )

    @JsonClass(generateAdapter = true)
    data class Thruster(
        @Json(name = "type") val type: String?,
        @Json(name = "amount") val amount: Int?,
        @Json(name = "pods") val pods: Int?,
        @Json(name = "fuel_1") val fuelOne: String?,
        @Json(name = "fuel_2") val fuelTwo: String?,
        @Json(name = "isp") val isp: Int?,
        @Json(name = "thrust") val thrust: Thrust?
    )

    @JsonClass(generateAdapter = true)
    data class Thrust(
        @Json(name = "kN") val kN: Double?,
        @Json(name = "lbf") val lbf: Double?
    )

    @JsonClass(generateAdapter = true)
    data class Mass(
        @Json(name = "kg") val kg: Double?,
        @Json(name = "lb") val lb: Double?
    )

    @JsonClass(generateAdapter = true)
    data class Volume(
        @Json(name = "cubic_meters") val cubicMetres: Double?,
        @Json(name = "cubic_feet") val cubicFeet: Double?
    )

    @JsonClass(generateAdapter = true)
    data class PressurizedCapsule(
        @Json(name = "payload_volume") val payloadVolume: Volume?
    )

    @JsonClass(generateAdapter = true)
    data class Trunk(
        @Json(name = "trunk_volume") val trunkVolume: Volume?,
        @Json(name = "cargo") val cargo: Cargo?
    ) {
        @JsonClass(generateAdapter = true)
        data class Cargo(
            @Json(name = "solar_array") val solarArray: Int?,
            @Json(name = "unpressurized_cargo") val unpressurizedCargo: Boolean?
        )
    }

    @JsonClass(generateAdapter = true)
    data class Length(
        @Json(name = "meters") val metres: Double?,
        @Json(name = "feet") val feet: Double?
    )
}

interface DragonsService {

    @GET("dragons")
    suspend fun all(): NetworkResponse<List<Dragon>, String>

    @GET("dragons/{id}")
    suspend fun one(
        @Path("id") id: String
    ): NetworkResponse<Dragon, String>
}
