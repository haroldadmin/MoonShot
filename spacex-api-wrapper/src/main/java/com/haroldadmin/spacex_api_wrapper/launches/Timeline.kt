package com.haroldadmin.spacex_api_wrapper.launches

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Timeline(
    @field:Json(name = "webcast_liftoff") val webcastLiftoff: Int,
    @field:Json(name = "go_for_prop_loading") val goForPropLoading : Int,
    @field:Json(name = "rp1_loading") val rp1Loading: Int,
    @field:Json(name = "stage1_lox_loading") val stage1LoxLoading: Int,
    @field:Json(name = "stage2_lox_loading") val stage2LoxLoading: Int,
    @field:Json(name = "engine_chill") val engineChill: Int,
    @field:Json(name = "prelaunch_checks") val prelaunchChecks: Int,
    @field:Json(name = "propellant_pressurization") val propellantPressurization: Int,
    @field:Json(name = "go_for_launch") val goForLaunch: Int,
    @field:Json(name = "ignition") val ignition: Int,
    @field:Json(name = "liftoff") val liftoff: Int,
    @field:Json(name = "maxq") val maxQ: Int,
    @field:Json(name = "meco") val meco: Int,
    @field:Json(name = "stage_sep") val stageSeparation : Int,
    @field:Json(name = "second_stage_ignition") val secondStateIgnition: Int,
    @field:Json(name = "fairing_deploy") val fairingDeploy: Int,
    @field:Json(name = "first_stage_entry_burn") val firstStageEntryBurn: Int,
    @field:Json(name = "seco-1") val seco1: Int,
    @field:Json(name = "first_stage_landing") val fistStageLanding: Int,
    @field:Json(name = "second_stage_restart") val secondStageRestart: Int,
    @field:Json(name = "seco-2") val seco2: Int,
    @field:Json(name = "payload_deploy") val payloadDeploy: Int
)