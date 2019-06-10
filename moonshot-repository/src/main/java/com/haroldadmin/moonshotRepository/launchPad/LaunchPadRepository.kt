package com.haroldadmin.moonshotRepository.launchPad

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.launchPad.LaunchPadDao
import com.haroldadmin.moonshotRepository.mappers.toDbLaunchPad
import com.haroldadmin.moonshot.models.launchpad.LaunchPad as DbLaunchPad
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPad
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPadService
import kotlinx.coroutines.flow.flow

class LaunchPadRepository(
    private val launchPadsService: LaunchPadService,
    private val launchPadDao: LaunchPadDao
) {

    suspend fun flowLaunchPad(siteId: String) = flow<Resource<DbLaunchPad>> {
        emit(Resource.Loading)
        val dbLaunchPad = launchPadDao.getLaunchPad(siteId)

        dbLaunchPad?.let {
            emit(Resource.Success(dbLaunchPad))
        }

        val apiResponse = executeWithRetry { launchPadsService.getLaunchPad(siteId).await() }

        when (apiResponse) {
            is NetworkResponse.Success -> {
                saveLaunchPad(apiResponse.body)
                val savedLaunchPad = launchPadDao.getLaunchPad(siteId)!!
                if (savedLaunchPad != dbLaunchPad) {
                    emit(Resource.Success(savedLaunchPad))
                }
            }
            is NetworkResponse.ServerError -> emit(Resource.Error(dbLaunchPad, apiResponse.body))
            is NetworkResponse.NetworkError -> emit(Resource.Error(dbLaunchPad, apiResponse.error))
        }
    }

    private suspend fun saveLaunchPad(launchPad: LaunchPad) {
        launchPadDao.save(launchPad.toDbLaunchPad())
    }
}