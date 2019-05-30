package com.haroldadmin.moonshot_repository.launch

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.cnradapter.invoke
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.safe
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot_repository.mappers.toDbLaunch
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class LaunchesRepository(
    private val localDataSource: LaunchDao,
    private val remoteDataSource: LaunchesService
) {

    suspend fun getAllLaunches(): Resource<List<Launch>> = withContext(Dispatchers.IO) {

        when (val launches = remoteDataSource.getAllLaunches().await()) {
            is NetworkResponse.Success -> {
                localDataSource.saveLaunches(launches.body.map { it.toDbLaunch() })
                Resource.Success(localDataSource.getAllLaunches())
            }

            is NetworkResponse.ServerError -> {
                Resource.Error(localDataSource.getAllLaunches(), launches.body)
            }

            is NetworkResponse.NetworkError -> {
                Resource.Error(localDataSource.getAllLaunches(), launches.error)
            }
            else -> {
                Resource.Error(localDataSource.getAllLaunches(), null)
            }
        }
    }

    suspend fun getUpcomingLaunches(currentTime: Long): Resource<List<Launch>> = withContext(Dispatchers.IO) {
        when (val launches = remoteDataSource.getUpcomingLaunches().await()) {
            is NetworkResponse.Success -> {
                localDataSource.saveLaunches(launches.body.map { it.toDbLaunch() })
                Resource.Success(localDataSource.getUpcomingLaunches(currentTime))
            }
            is NetworkResponse.ServerError -> {
                Resource.Error(localDataSource.getUpcomingLaunches(currentTime), launches.body)
            }

            is NetworkResponse.NetworkError -> {
                Resource.Error(localDataSource.getUpcomingLaunches(currentTime), launches.error)
            }

            else -> {
                Resource.Error(localDataSource.getUpcomingLaunches(currentTime), null)
            }
        }
    }

    suspend fun getNextLaunch(currentTime: Long): Resource<Launch> = withContext(Dispatchers.IO) {
        val launch = executeWithRetry { remoteDataSource.getNextLaunch().await() }
        when (launch) {
            is NetworkResponse.Success -> {
                val nextLaunch = launch()!!.toDbLaunch()
                localDataSource.saveLaunch(nextLaunch)
                Resource.Success(localDataSource.getNextLaunch(currentTime))
            }
            is NetworkResponse.ServerError -> {
                Resource.Error(localDataSource.getNextLaunch(currentTime), launch.body)
            }
            is NetworkResponse.NetworkError -> {
                Resource.Error(localDataSource.getNextLaunch(currentTime), launch.error)
            }
        }
    }

    suspend fun getPastLaunches(currentTime: Long): Resource<List<Launch>> = withContext(Dispatchers.IO) {

        when (val launches = remoteDataSource.getPastLaunches().await()) {
            is NetworkResponse.Success -> {
                localDataSource.saveLaunches(launches.body.map { it.toDbLaunch() })
                Resource.Success(localDataSource.getPastLaunches(currentTime))
            }

            is NetworkResponse.ServerError -> {
                Resource.Error(localDataSource.getPastLaunches(currentTime), launches.body)
            }

            is NetworkResponse.NetworkError -> {
                Resource.Error(localDataSource.getPastLaunches(currentTime), launches.error)
            }

            else -> {
                Resource.Error(localDataSource.getPastLaunches(currentTime), null)
            }
        }
    }

    @FlowPreview
    suspend fun flowAllLaunches() =
        flow {
                emit(Resource.Loading)

                val dbLaunches = localDataSource.getAllLaunches()
                emit(Resource.Success(dbLaunches))

                when (val launches = remoteDataSource.getAllLaunches().await()) {
                    is NetworkResponse.Success -> {
                        localDataSource.saveLaunches(launches.body.map { it.toDbLaunch() })
                        val savedLaunches = localDataSource.getAllLaunches()
                        emit(Resource.Success(savedLaunches))
                    }
                    is NetworkResponse.ServerError -> {
                        emit(Resource.Error(dbLaunches, launches.body))
                    }
                    is NetworkResponse.NetworkError -> {
                        emit(Resource.Error(dbLaunches, launches.error))
                    }
                }
            }
            .flowOn(Dispatchers.IO, bufferSize = 0)
}