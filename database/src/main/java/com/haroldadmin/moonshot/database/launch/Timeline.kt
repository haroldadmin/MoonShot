package com.haroldadmin.moonshot.database.launch

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timelines")
data class Timeline(
    @ColumnInfo(name = "webcast_liftoff")
    val webcastLiftoff: Int,
    @ColumnInfo(name = "go_for_prop_loading")
    val goForPropLoading: Int,
    @ColumnInfo(name = "rp1_loading")
    val rp1Loading: Int,
    @ColumnInfo(name = "stage1_lox_loading")
    val stage1LoxLoading: Int,
    @ColumnInfo(name = "stage2_lox_loading")
    val stage2LoxLoading: Int,
    @ColumnInfo(name = "engine_chill")
    val engineChill: Int,
    @ColumnInfo(name = "prelaunch_checks")
    val prelaunchChecks: Int,
    @ColumnInfo(name = "propellant_pressurization")
    val propellantPressurization: Int,
    @ColumnInfo(name = "go_for_launch")
    val goForLaunch: Int,
    @ColumnInfo(name = "ignition")
    val ignition: Int,
    @ColumnInfo(name = "liftoff")
    val liftoff: Int,
    @ColumnInfo(name = "maxq")
    val maxq: Int,
    @ColumnInfo(name = "meco")
    val meco: Int,
    @ColumnInfo(name = "stage_separation")
    val stageSeparation: Int,
    @ColumnInfo(name = "second_stage_ignition")
    val secondStageIgnition: Int,
    @ColumnInfo(name = "fairing_deploy")
    val fairingDeploy: Int,
    @ColumnInfo(name = "first_stage_entry_burn")
    val firstStageEntryBurn: Int,
    @ColumnInfo(name = "seco-1")
    val seco1: Int,
    @ColumnInfo(name = "first_stage_landing")
    val firstStageLanding: Int,
    @ColumnInfo(name = "second_stage_restart")
    val secondStageRestart: Int,
    @ColumnInfo(name = "seco-2")
    val seco2: Int,
    @ColumnInfo(name = "payload_deploy")
    val payloadDeploy: Int
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "timeline_id")
    var id: Int? = null

}