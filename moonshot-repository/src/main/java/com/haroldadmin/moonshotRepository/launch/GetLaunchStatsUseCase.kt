package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.LaunchStats
import com.haroldadmin.moonshotRepository.dbBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetLaunchStatsUseCase(
    private val launchesDao: LaunchDao
) {
    @ExperimentalCoroutinesApi
    suspend fun getLaunchStats(flightNumber: Int): Flow<Resource<LaunchStats>> {
        return dbBoundResource(
            dbFetcher = { getLaunchStatsCached(flightNumber) },
            validator = { data -> data != null }
        )
    }

    private suspend fun getLaunchStatsCached(flightNumber: Int) = withContext(Dispatchers.IO) {
        launchesDao.getLaunchStats(flightNumber)
    }
}