package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.LaunchPictures
import com.haroldadmin.moonshotRepository.dbBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetLaunchPicturesUseCase(
    private val launchesDao: LaunchDao
) {

    @ExperimentalCoroutinesApi
    suspend fun getLaunchPictures(flightNumber: Int): Flow<Resource<LaunchPictures>> {
        return dbBoundResource(
            dbFetcher = { getLaunchPicturesCached(flightNumber) },
            validator = { data -> data != null }
        )
    }

    private suspend fun getLaunchPicturesCached(flightNumber: Int) = withContext(Dispatchers.IO) {
        launchesDao.getLaunchPictures(flightNumber)
    }
}