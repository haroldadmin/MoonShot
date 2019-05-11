package com.haroldadmin.spacex_api_wrapper.launches

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Timeline(
    @Json(name = "webcast_liftoff") val webcastLiftoff: Int,
    @Json(name = "go_for_prop_loading") val goForPropLoading : Int,
    @Json(name = "rp1_loading") val rp1Loading: Int,
    @Json(name = "stage1_lox_loading") val stage1LoxLoading: Int,
    @Json(name = "stage2_lox_loading") val stage2LoxLoading: Int,
    @Json(name = "engine_chill") val engineChill: Int,
    @Json(name = "prelaunch_checks") val prelaunchChecks: Int,
    @Json(name = "propellant_pressurization") val propellantPressurization: Int,
    @Json(name = "go_for_launch") val goForLaunch: Int,
    @Json(name = "ignition") val ignition: Int,
    @Json(name = "liftoff") val liftoff: Int,
    @Json(name = "maxq") val maxQ: Int,
    @Json(name = "meco") val meco: Int,
    @Json(name = "stage_sep") val stageSeparation : Int,
    @Json(name = "second_stage_ignition") val secondStateIgnition: Int,
    @Json(name = "fairing_deploy") val fairingDeploy: Int,
    @Json(name = "first_stage_entry_burn") val firstStageEntryBurn: Int,
    @Json(name = "seco-1") val seco1: Int,
    @Json(name = "first_stage_landing") val fistStageLanding: Int,
    @Json(name = "second_stage_restart") val secondStageRestart: Int,
    @Json(name = "seco-2") val seco2: Int,
    @Json(name = "payload_deploy") val payloadDeploy: Int
)