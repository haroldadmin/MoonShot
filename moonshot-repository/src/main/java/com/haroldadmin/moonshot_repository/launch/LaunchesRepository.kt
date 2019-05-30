package com.haroldadmin.moonshot_repository.launch

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.cnradapter.invoke
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.database.launch.rocket.RocketSummaryDao
import com.haroldadmin.moonshot.database.launch.rocket.first_stage.FirstStageSummaryDao
import com.haroldadmin.moonshot.database.launch.rocket.second_stage.SecondStageSummaryDao
import com.haroldadmin.moonshot_repository.mappers.*
import com.haroldadmin.spacex_api_wrapper.launches.Launch
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import com.haroldadmin.moonshot.models.launch.Launch as DbLaunch

class LaunchesRepository(
    private val launchDao: LaunchDao,
    private val rocketSummaryDao: RocketSummaryDao,
    private val firstStageSummaryDao: FirstStageSummaryDao,
    private val secondStageSummaryDao: SecondStageSummaryDao,
    private val launchesService: LaunchesService
) {

    suspend fun getAllLaunches(): Resource<List<DbLaunch>> = withContext(Dispatchers.IO) {

        when (val launches = launchesService.getAllLaunches().await()) {
            is NetworkResponse.Success -> {
                saveApiLaunchesToDb(launches()!!)
                Resource.Success(launchDao.getAllLaunches())
            }

            is NetworkResponse.ServerError -> {
                Resource.Error(launchDao.getAllLaunches(), launches.body)
            }

            is NetworkResponse.NetworkError -> {
                Resource.Error(launchDao.getAllLaunches(), launches.error)
            }
            else -> {
                Resource.Error(launchDao.getAllLaunches(), null)
            }
        }
    }

    suspend fun getUpcomingLaunches(currentTime: Long): Resource<List<DbLaunch>> = withContext(Dispatchers.IO) {
        when (val launchesResponse = launchesService.getUpcomingLaunches().await()) {
            is NetworkResponse.Success -> {
                saveApiLaunchesToDb(launchesResponse()!!)
                Resource.Success(launchDao.getUpcomingLaunches(currentTime))
            }
            is NetworkResponse.ServerError -> {
                Resource.Error(launchDao.getUpcomingLaunches(currentTime), launchesResponse.body)
            }

            is NetworkResponse.NetworkError -> {
                Resource.Error(launchDao.getUpcomingLaunches(currentTime), launchesResponse.error)
            }

            else -> {
                Resource.Error(launchDao.getUpcomingLaunches(currentTime), null)
            }
        }
    }

    suspend fun getNextLaunch(currentTime: Long): Resource<DbLaunch> = withContext(Dispatchers.IO) {
        val launch = executeWithRetry { launchesService.getNextLaunch().await() }
        when (launch) {
            is NetworkResponse.Success -> {
                saveApiLaunchToDb(launch()!!)
                Resource.Success(launchDao.getNextLaunch(currentTime))
            }
            is NetworkResponse.ServerError -> {
                Resource.Error(launchDao.getNextLaunch(currentTime), launch.body)
            }
            is NetworkResponse.NetworkError -> {
                Resource.Error(launchDao.getNextLaunch(currentTime), launch.error)
            }
        }
    }

    suspend fun getPastLaunches(currentTime: Long): Resource<List<DbLaunch>> = withContext(Dispatchers.IO) {

        when (val launches = launchesService.getPastLaunches().await()) {
            is NetworkResponse.Success -> {
                saveApiLaunchesToDb(launches()!!)
                Resource.Success(launchDao.getPastLaunches(currentTime))
            }

            is NetworkResponse.ServerError -> {
                Resource.Error(launchDao.getPastLaunches(currentTime), launches.body)
            }

            is NetworkResponse.NetworkError -> {
                Resource.Error(launchDao.getPastLaunches(currentTime), launches.error)
            }

            else -> {
                Resource.Error(launchDao.getPastLaunches(currentTime), null)
            }
        }
    }

    @FlowPreview
    suspend fun flowAllLaunches() =
        flow {
            emit(Resource.Loading)

            val dbLaunches = launchDao.getAllLaunches()
            emit(Resource.Success(dbLaunches))

            when (val launches = launchesService.getAllLaunches().await()) {
                is NetworkResponse.Success -> {
                    saveApiLaunchesToDb(launches()!!)
                    val savedLaunches = launchDao.getAllLaunches()
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

    private suspend fun saveApiLaunchesToDb(launches: List<Launch>) {
        for (launch in launches) {
            saveApiLaunchToDb(launch)
        }
    }

    private suspend fun saveApiLaunchToDb(launch: Launch) {
        println("Saving launch with flight Id ${launch.flightNumber}")
        launchDao.saveLaunch(launch.toDbLaunch())
        println("Saved launch")
        println("Saving rocket summer for flight id: ${launch.flightNumber}, rocketId = ${launch.rocket.rocketId}")
        rocketSummaryDao.saveRocketSummary(launch.rocket.toDbRocketSummary(launch.flightNumber))
        println("Saved rocket summary")

        println("Saving fss for rocketId: ${launch.rocket.rocketId}")
        val fssId = firstStageSummaryDao.saveFirstStageSummary(
            launch.rocket.firstStage.toDbFirstStageSummary(launch.rocket.rocketId)
        )
        println("Saved fss")
        println("Saving core summaries for fss id: $fssId")
        firstStageSummaryDao.saveCoreSummaries(launch.rocket.firstStage.cores.map { it.toDbCoreSummary(fssId) })
        println("Saved core summaries")

        println("Saving sss for rocketId: ${launch.rocket.rocketId}")
        val sssId = secondStageSummaryDao.saveSecondStageSummary(
            launch.rocket.secondState.toDbSecondStageSummary(launch.rocket.rocketId)
        )
        println("Saved sss")

        println("Saving payloads for sss: $sssId")
        secondStageSummaryDao.savePayloads(launch.rocket.secondState.payloads.map { it.toDbPayload(sssId) })
        println("Saved paylaods")
    }
}