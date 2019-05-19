package com.haroldadmin.moonshot_repository

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot_repository.mappers.toDbLaunch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LaunchesRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun getAllLaunches(): Resource<List<Launch>> {
        val launches = remoteDataSource.getAllLaunches()

        return when (launches) {
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
        }
    }
}