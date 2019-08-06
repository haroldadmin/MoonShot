package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshotRepository.mappers.toDbCoreSummary
import com.haroldadmin.moonshotRepository.mappers.toDbFirstStageSummary
import com.haroldadmin.moonshotRepository.mappers.toDbLaunch
import com.haroldadmin.moonshotRepository.mappers.toDbPayload
import com.haroldadmin.moonshotRepository.mappers.toDbRocketSummary
import com.haroldadmin.moonshotRepository.mappers.toDbSecondStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary as DbRocketSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.CoreSummary as DbCoreSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.FirstStageSummary as DbFirstStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.SecondStageSummary as DbSecondStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.payload.Payload as DbPayload
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.launches.Launch as ApiLaunch
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class GetAllLaunchesUseCase(
    launchesDao: LaunchDao,
    launchesService: LaunchesService
): LaunchesUseCase(launchesDao, launchesService) {

    suspend fun getAllLaunches(limit: Int = Integer.MAX_VALUE): Flow<Resource<List<LaunchMinimal>>> {
        return flow {
            emit(Resource.Loading)

            val cachedDbLaunches = getLaunchesFromDatabase(limit)

            if (cachedDbLaunches.isNotEmpty()) {
                emit(Resource.Success(data = cachedDbLaunches, isCached = true))
            }

            when (val apiLaunches = getLaunchesFromService()) {
                is NetworkResponse.Success -> {
                    val data = apiLaunches.body
                    persistLaunches(data)
                    val refreshedDbLaunches = getLaunchesFromDatabase(limit)
                    emit(Resource.Success(data = refreshedDbLaunches, isCached = false))
                }
                is NetworkResponse.NetworkError -> {
                    val error = apiLaunches.error
                    emit(Resource.Error(data = cachedDbLaunches, error = error))
                }

                is NetworkResponse.ServerError -> {
                    val error = apiLaunches.body
                    emit(Resource.Error(data = cachedDbLaunches, error = error))
                }
            }

        }
    }

    private suspend fun getLaunchesFromDatabase(limit: Int): List<LaunchMinimal> =
        withContext(Dispatchers.IO) {
            launchesDao.getAllLaunchesMinimal(
                maxTimeStamp = Long.MAX_VALUE,
                minTimeStamp = Long.MIN_VALUE,
                limit = limit
            )
        }

    private suspend fun getLaunchesFromService(): NetworkResponse<List<ApiLaunch>, ErrorResponse> =
        withContext(Dispatchers.IO) {
            executeWithRetry {
                launchesService.getAllLaunches().await()
            }
        }
}