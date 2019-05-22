package com.haroldadmin.moonshot_repository

import com.haroldadmin.moonshot.database.capsule.CapsuleDao
import com.haroldadmin.moonshot.database.core.CoreDao
import com.haroldadmin.moonshot.database.dragons.DragonsDao
import com.haroldadmin.moonshot.database.dragons.ThrustersDao
import com.haroldadmin.moonshot.database.historical_event.HistoricalEventsDao
import com.haroldadmin.moonshot.database.info.CompanyInfoDao
import com.haroldadmin.moonshot.database.landing_pad.LandingPadDao
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.database.launch.rocket.RocketSummaryDao
import com.haroldadmin.moonshot.database.launch.rocket.first_stage.FirstStageSummaryDao
import com.haroldadmin.moonshot.database.launch.rocket.second_stage.SecondStageSummaryDao
import com.haroldadmin.moonshot.models.launch.Launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSource(
    private val firstStageSummaryDao: FirstStageSummaryDao,
    private val secondStageSummaryDao: SecondStageSummaryDao,
    private val rocketSummaryDao: RocketSummaryDao,
    private val launchDao: LaunchDao,
    private val landingPadDao: LandingPadDao,
    private val companyInfoDao: CompanyInfoDao,
    private val historicalEventsDao: HistoricalEventsDao,
    private val thrustersDao: ThrustersDao,
    private val dragonsDao: DragonsDao,
    private val coreDao: CoreDao,
    private val capsuleDao: CapsuleDao
) {

    suspend fun getAllLaunches(): List<Launch> = withContext(Dispatchers.IO) {
        launchDao.getAllLaunches()
    }

    suspend fun saveLaunches(launches: List<Launch>) = withContext(Dispatchers.IO) {
        launchDao.saveLaunches(launches)
    }

}