package com.haroldadmin.moonshot.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.haroldadmin.moonshot.database.capsule.Capsule
import com.haroldadmin.moonshot.database.capsule.CapsuleDao
import com.haroldadmin.moonshot.database.common.Length
import com.haroldadmin.moonshot.database.common.Location
import com.haroldadmin.moonshot.database.common.Mass
import com.haroldadmin.moonshot.database.common.MissionSummary
import com.haroldadmin.moonshot.database.common.MissionSummaryDao
import com.haroldadmin.moonshot.database.common.Thrust
import com.haroldadmin.moonshot.database.common.Volume
import com.haroldadmin.moonshot.database.core.Core
import com.haroldadmin.moonshot.database.core.CoreDao
import com.haroldadmin.moonshot.database.dragon.Cargo
import com.haroldadmin.moonshot.database.dragon.Dragon
import com.haroldadmin.moonshot.database.dragon.HeatShield
import com.haroldadmin.moonshot.database.dragon.PressurizedCapsule
import com.haroldadmin.moonshot.database.dragon.Thruster
import com.haroldadmin.moonshot.database.dragon.Trunk
import com.haroldadmin.moonshot.database.history.HistoricalEvent
import com.haroldadmin.moonshot.database.history.Links
import com.haroldadmin.moonshot.database.info.CompanyInfo
import com.haroldadmin.moonshot.database.info.Headquarters
import com.haroldadmin.moonshot.database.landingpad.LandingPad
import com.haroldadmin.moonshot.database.launch.rocket.first_stage.CoreSummary
import com.haroldadmin.moonshot.database.launch.rocket.Fairings
import com.haroldadmin.moonshot.database.launch.rocket.first_stage.FirstStageSummary
import com.haroldadmin.moonshot.database.launch.rocket.first_stage.FirstStageSummaryDao
import com.haroldadmin.moonshot.database.launch.Launch
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.database.launch.LaunchSite
import com.haroldadmin.moonshot.database.launch.rocket.second_stage.payload.OrbitParams
import com.haroldadmin.moonshot.database.launch.rocket.second_stage.payload.Payload
import com.haroldadmin.moonshot.database.launch.rocket.RocketSummary
import com.haroldadmin.moonshot.database.launch.rocket.RocketSummaryDao
import com.haroldadmin.moonshot.database.launch.rocket.second_stage.SecondStageSummary
import com.haroldadmin.moonshot.database.launch.rocket.second_stage.SecondStageSummaryDao
import com.haroldadmin.moonshot.database.launch.Telemetry
import com.haroldadmin.moonshot.database.launch.Timeline
import com.haroldadmin.moonshot.database.launch.Links as LaunchLinks

@Database(
    entities = arrayOf(
        Capsule::class,
        Length::class,
        Location::class,
        Mass::class,
        MissionSummary::class,
        Thrust::class,
        Volume::class,
        Core::class,
        Cargo::class,
        Dragon::class,
        HeatShield::class,
        PressurizedCapsule::class,
        Thruster::class,
        Trunk::class,
        HistoricalEvent::class,
        Links::class,
        CompanyInfo::class,
        Headquarters::class,
        LandingPad::class,
        CoreSummary::class,
        FirstStageSummary::class,
        Launch::class,
        Payload::class,
        RocketSummary::class,
        SecondStageSummary::class
    ), version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MoonShotDb : RoomDatabase() {

    abstract fun capsuleDao(): CapsuleDao
    abstract fun missionSummaryDao(): MissionSummaryDao
    abstract fun coreDao(): CoreDao

    abstract fun firstStageSummaryDao(): FirstStageSummaryDao
    abstract fun secondStageSummaryDao(): SecondStageSummaryDao
    abstract fun rocketSummaryDao(): RocketSummaryDao
    abstract fun launchDao(): LaunchDao

}