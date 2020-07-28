package com.haroldadmin.moonshot.features.launch.next

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.core.AppDispatchers
import com.haroldadmin.moonshot.core.Result
import com.haroldadmin.moonshot.core.safe
import com.haroldadmin.moonshot.db.Launch
import com.haroldadmin.moonshot.db.LaunchQueries
import com.haroldadmin.moonshot.db.toDBModel
import com.haroldadmin.moonshot.services.spacex.v4.APIException
import com.haroldadmin.moonshot.services.spacex.v4.LaunchesService
import com.haroldadmin.moonshot.services.spacex.v4.Launch as APILaunch
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NextLaunchUsecase @Inject constructor(
    private val launchQueries: LaunchQueries,
    private val launchesService: LaunchesService,
    private val appDispatchers: AppDispatchers
) {

    fun flow(): Flow<Launch> {
        return launchQueries.next()
            .asFlow()
            .mapToOne()
            .flowOn(appDispatchers.io)
    }

    suspend fun refresh(): Result<APILaunch> {
        return withContext(appDispatchers.io) {
           when (val response = launchesService.next()) {
               is NetworkResponse.Success -> {
                   response.body.toDBModel().let {
                       launchQueries.save(it)
                   }
                   Result.Success(response.body)
               }
               is NetworkResponse.ServerError -> {
                   Result.Error<APILaunch>(APIException(response.body))
               }
               is NetworkResponse.NetworkError -> Result.Error(response.error)
               is NetworkResponse.UnknownError -> Result.Error(response.error)
           }
        }
    }
}