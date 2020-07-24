package com.haroldadmin.moonshot.features.launches

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.base.di.V4API
import com.haroldadmin.moonshot.core.AppDispatchers
import com.haroldadmin.moonshot.core.Result
import com.haroldadmin.moonshot.db.Launch
import com.haroldadmin.moonshot.db.LaunchQueries
import com.haroldadmin.moonshot.db.toDBModel
import com.haroldadmin.moonshot.services.spacex.v4.APIException
import com.haroldadmin.moonshot.services.spacex.v4.LaunchesService
import com.haroldadmin.moonshot.services.spacex.v4.Launch as APILaunch
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AllLaunchesUsecase @Inject constructor(
    private val launchQueries: LaunchQueries,
    @V4API private val launchesService: LaunchesService,
    private val appDispatchers: AppDispatchers
) {
    fun observe(
        limit: Long = 20,
        offset: Long = 0
    ): Flow<List<Launch>> {
        return launchQueries
            .allAscending(limit, offset)
            .asFlow()
            .mapToList()
            .flowOn(appDispatchers.io)
    }

    suspend fun refresh(): Result<List<APILaunch>> {
        return withContext(appDispatchers.io) {
            when (val response = launchesService.all()) {
                is NetworkResponse.Success -> {
                    launchQueries.transaction {
                        response.body.forEach { apiLaunch ->
                            launchQueries.save(apiLaunch.toDBModel())
                        }
                    }
                    Result.Success(response.body)
                }
                is NetworkResponse.ServerError -> {
                    Result.Error<List<APILaunch>>(APIException(response.body))
                }
                is NetworkResponse.NetworkError -> {
                    Result.Error(response.error)
                }
                is NetworkResponse.UnknownError -> {
                    Result.Error(response.error)
                }
            }
        }
    }
}