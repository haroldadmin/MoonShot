package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.cnradapter.invoke
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.database.launch.rocket.RocketSummaryDao
import com.haroldadmin.moonshot.database.launch.rocket.firstStage.FirstStageSummaryDao
import com.haroldadmin.moonshot.database.launch.rocket.secondStage.SecondStageSummaryDao
import com.haroldadmin.moonshotRepository.mappers.toDbCoreSummary
import com.haroldadmin.moonshotRepository.mappers.toDbFirstStageSummary
import com.haroldadmin.moonshotRepository.mappers.toDbLaunch
import com.haroldadmin.moonshotRepository.mappers.toDbPayload
import com.haroldadmin.moonshotRepository.mappers.toDbRocketSummary
import com.haroldadmin.moonshotRepository.mappers.toDbSecondStageSummary
import com.haroldadmin.spacex_api_wrapper.launches.Launch
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import com.haroldadmin.moonshot.models.launch.Launch as DbLaunch
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary as DbRocketSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.CoreSummary as DbCoreSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.FirstStageSummary as DbFirstStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.SecondStageSummary as DbSecondStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.payload.Payload as DbPayload

class LaunchesRepository(
    private val launchDao: LaunchDao,
    private val rocketSummaryDao: RocketSummaryDao,
    private val firstStageSummaryDao: FirstStageSummaryDao,
    private val secondStageSummaryDao: SecondStageSummaryDao,
    private val launchesService: LaunchesService
) {

    suspend fun getAllLaunches(
        limit: Int = 20,
        maxTimestamp: Long = Long.MAX_VALUE
    ): Resource<List<DbLaunch>> = withContext(Dispatchers.IO) {

        when (val launches = launchesService.getAllLaunches().await()) {
            is NetworkResponse.Success -> {
                saveApiLaunches(launches()!!)
                Resource.Success(launchDao.getAllLaunches(maxTimestamp, limit))
            }

            is NetworkResponse.ServerError -> {
                Resource.Error(launchDao.getAllLaunches(limit), launches.body)
            }

            is NetworkResponse.NetworkError -> {
                Resource.Error(launchDao.getAllLaunches(limit), launches.error)
            }
            else -> {
                Resource.Error(launchDao.getAllLaunches(limit), null)
            }
        }
    }

    suspend fun getUpcomingLaunches(currentTime: Long): Resource<List<DbLaunch>> = withContext(Dispatchers.IO) {
        when (val launchesResponse = launchesService.getUpcomingLaunches().await()) {
            is NetworkResponse.Success -> {
                saveApiLaunches(launchesResponse()!!)
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
                saveApiLaunch(launch()!!)
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
                saveApiLaunches(launches()!!)
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
    suspend fun flowAllLaunches(limit: Int) =
        flow {
            emit(Resource.Loading)

            val dbLaunches = launchDao.getAllLaunches(limit)
            emit(Resource.Success(dbLaunches))

            when (val launches = launchesService.getAllLaunches().await()) {
                is NetworkResponse.Success -> {
                    saveApiLaunches(launches()!!)
                    val savedLaunches = launchDao.getAllLaunches(limit)
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


    suspend fun getLaunch(flightNumber: Int): Resource<DbLaunch> = withContext(Dispatchers.IO) {
        val launch = executeWithRetry { launchesService.getLaunch(flightNumber).await() }
        when (launch) {
            is NetworkResponse.Success -> {
                saveApiLaunch(launch()!!)
                Resource.Success(launchDao.getLaunch(flightNumber))
            }

            is NetworkResponse.ServerError -> {
                Resource.Error(launchDao.getLaunch(flightNumber), launch.body)
            }
            is NetworkResponse.NetworkError -> {
                Resource.Error(launchDao.getLaunch(flightNumber), launch.error)
            }
        }
    }

    suspend fun getRocketSummary(flightNumber: Int): Resource<DbRocketSummary> = withContext(Dispatchers.IO) {
        Resource.Success(rocketSummaryDao.getRocketSummaryForLaunch(flightNumber))
    }

    private suspend fun saveApiLaunches(apiLaunches: List<Launch>) {
        val launches: List<DbLaunch> = apiLaunches.map { it.toDbLaunch() }

        val rocketSummaries: List<DbRocketSummary> =
            apiLaunches.map { launch -> launch.rocket.toDbRocketSummary(launch.flightNumber) }

        val firstStageSummaries: List<DbFirstStageSummary> =
            apiLaunches.map { launch -> launch.rocket.firstStage.toDbFirstStageSummary(launch.flightNumber) }

        val secondStageSummaries: List<DbSecondStageSummary> =
            apiLaunches.map { launch -> launch.rocket.secondState.toDbSecondStageSummary(launch.flightNumber) }

        val coreSummaries: List<DbCoreSummary> = apiLaunches.flatMap { launch ->
            launch.rocket.firstStage.cores.map { core -> core.toDbCoreSummary(launch.flightNumber) }
        }

        val payloads: List<DbPayload> = apiLaunches.flatMap { launch ->
            launch.rocket.secondState.payloads.map { payload -> payload.toDbPayload(launch.flightNumber) }
        }
        launchDao.saveLaunchesWithSummaries(
            launches,
            rocketSummaries,
            firstStageSummaries,
            coreSummaries,
            secondStageSummaries,
            payloads
        )
    }

    private suspend fun saveApiLaunch(apiLaunch: Launch) {
        val launch = apiLaunch.toDbLaunch()

        val rocketSummary = apiLaunch.rocket.toDbRocketSummary(apiLaunch.flightNumber)

        val firstStageSummary = apiLaunch.rocket.firstStage.toDbFirstStageSummary(apiLaunch.flightNumber)

        val secondStageSummary = apiLaunch.rocket.secondState.toDbSecondStageSummary(apiLaunch.flightNumber)

        val coreSummaries =
            apiLaunch.rocket.firstStage.cores.map { coreSummary -> coreSummary.toDbCoreSummary(apiLaunch.flightNumber) }

        val payloads =
            apiLaunch.rocket.secondState.payloads.map { payload -> payload.toDbPayload(apiLaunch.flightNumber) }

        launchDao.saveLaunchWithSummaries(
            launch,
            rocketSummary,
            firstStageSummary,
            secondStageSummary,
            coreSummaries,
            payloads
        )
    }
}