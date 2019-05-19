package com.haroldadmin.moonshot_repository

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
    private val launchDao: LaunchDao
) {

    suspend fun getAllLaunches(): List<Launch> = withContext(Dispatchers.IO) {
        launchDao.getAllLaunches()
    }

    suspend fun saveLaunches(launches: List<Launch>) = withContext(Dispatchers.IO) {
        launchDao.saveLaunches(launches)
    }

}