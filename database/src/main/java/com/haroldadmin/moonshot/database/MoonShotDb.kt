package com.haroldadmin.moonshot.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.haroldadmin.moonshot.models.Capsule
import com.haroldadmin.moonshot.models.CompanyInfo
import com.haroldadmin.moonshot.models.Core
import com.haroldadmin.moonshot.models.Dragon
import com.haroldadmin.moonshot.models.HistoricalEvent
import com.haroldadmin.moonshot.models.LandingPad
import com.haroldadmin.moonshot.models.LaunchPad
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.Rocket

@Database(
    entities = [
        Launch::class,
        Capsule::class,
        CompanyInfo::class,
        Core::class,
        Dragon::class,
        HistoricalEvent::class,
        LandingPad::class,
        LaunchPad::class,
        Rocket::class
    ], version = 2, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MoonShotDb : RoomDatabase() {

    abstract fun launchDao(): LaunchDao
    abstract fun rocketsDao(): RocketsDao
    abstract fun launchPadDao(): LaunchPadDao

//    abstract fun capsuleDao(): CapsuleDao
//    abstract fun missionSummaryDao(): MissionSummaryDao
//    abstract fun coreDao(): CoreDao
//
//    abstract fun firstStageSummaryDao(): FirstStageSummaryDao
//    abstract fun secondStageSummaryDao(): SecondStageSummaryDao
//    abstract fun rocketSummaryDao(): RocketSummaryDao
//    abstract fun launchDao(): LaunchDao
//
//    abstract fun thrustersDao(): ThrustersDao
//    abstract fun dragonsDao(): DragonsDao
//
//    abstract fun historicalEventsDao(): HistoricalEventsDao
//
//    abstract fun companyInfoDao(): CompanyInfoDao
//
//    abstract fun landingPadDao(): LandingPadDao
//
//    abstract fun payloadWeightsDao(): PayloadWeightsDao
//    abstract fun rocketsDao(): RocketsDao
//
//    abstract fun launchpadDao(): LaunchPadDao
}