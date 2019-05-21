package com.haroldadmin.moonshot.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.haroldadmin.moonshot.database.capsule.CapsuleDao
import com.haroldadmin.moonshot.database.common.MissionSummaryDao
import com.haroldadmin.moonshot.database.core.CoreDao
import com.haroldadmin.moonshot.database.dragons.DragonsDao
import com.haroldadmin.moonshot.database.dragons.ThrustersDao
import com.haroldadmin.moonshot.database.historical_event.HistoricalEventsDao
import com.haroldadmin.moonshot.database.info.CompanyInfoDao
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.database.launch.rocket.RocketSummaryDao
import com.haroldadmin.moonshot.database.launch.rocket.first_stage.FirstStageSummaryDao
import com.haroldadmin.moonshot.database.launch.rocket.second_stage.SecondStageSummaryDao
import com.haroldadmin.moonshot.models.capsule.Capsule
import com.haroldadmin.moonshot.models.common.Length
import com.haroldadmin.moonshot.models.common.Location
import com.haroldadmin.moonshot.models.common.Mass
import com.haroldadmin.moonshot.models.common.MissionSummary
import com.haroldadmin.moonshot.models.common.Thrust
import com.haroldadmin.moonshot.models.common.Volume
import com.haroldadmin.moonshot.models.core.Core
import com.haroldadmin.moonshot.models.dragon.Dragon
import com.haroldadmin.moonshot.models.dragon.Thruster
import com.haroldadmin.moonshot.models.history.HistoricalEvent
import com.haroldadmin.moonshot.models.history.Links
import com.haroldadmin.moonshot.models.info.CompanyInfo
import com.haroldadmin.moonshot.models.info.Headquarters
import com.haroldadmin.moonshot.models.landingpad.LandingPad
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.CoreSummary
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.FirstStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.SecondStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.payload.Payload
import com.haroldadmin.moonshot.models.launch.Links as LaunchLinks

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
        Dragon::class,
        Thruster::class,
        HistoricalEvent::class,
        CompanyInfo::class,
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

    abstract fun thrustersDao(): ThrustersDao
    abstract fun dragonsDao(): DragonsDao

    abstract fun historicalEventsDao(): HistoricalEventsDao

    abstract fun companyInfoDao(): CompanyInfoDao
}