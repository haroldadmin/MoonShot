package com.haroldadmin.moonshot.models.launch

data class Timeline(
    val webcastLiftoff: Int?,
    val goForPropLoading: Int?,
    val rp1Loading: Int?,
    val stage1LoxLoading: Int?,
    val stage2LoxLoading: Int?,
    val engineChill: Int?,
    val prelaunchChecks: Int?,
    val propellantPressurization: Int?,
    val goForLaunch: Int?,
    val ignition: Int?,
    val liftoff: Int?,
    val maxq: Int?,
    val meco: Int?,
    val stageSeparation: Int?,
    val secondStageIgnition: Int?,
    val fairingDeploy: Int?,
    val firstStageEntryBurn: Int?,
    val seco1: Int?,
    val firstStageLanding: Int?,
    val secondStageRestart: Int?,
    val seco2: Int?,
    val payloadDeploy: Int?
) {
    companion object {
        fun getSampleTimeline() = Timeline(
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0
        )
    }
}