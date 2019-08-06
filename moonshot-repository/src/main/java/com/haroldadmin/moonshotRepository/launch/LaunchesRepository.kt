@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.executeWithRetry
import com.haroldadmin.cnradapter.invoke
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.database.launch.rocket.RocketSummaryDao
import com.haroldadmin.moonshot.database.launch.rocket.firstStage.FirstStageSummaryDao
import com.haroldadmin.moonshot.database.launch.rocket.secondStage.SecondStageSummaryDao
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.launch.LaunchPictures
import com.haroldadmin.moonshot.models.launch.LaunchStats
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

    @FlowPreview
    suspend fun flowAllMinimalLaunches(
        limit: Int,
        maxTimestamp: Long = Long.MAX_VALUE,
        minTimeStamp: Long = Long.MIN_VALUE
    ) = flow {
        emit(Resource.Loading)

        val dbLaunches = launchDao.getAllLaunchesMinimal(maxTimestamp, minTimeStamp, limit)

        if (!dbLaunches.isNullOrEmpty()) {
            emit(Resource.Success(dbLaunches))
        }

        when (val launches = launchesService.getAllLaunches().await()) {
            is NetworkResponse.Success -> {
                saveApiLaunches(launches()!!)
                val savedLaunches = launchDao.getAllLaunchesMinimal(maxTimestamp, minTimeStamp, limit)
                if (savedLaunches != dbLaunches) {
                    emit(Resource.Success(savedLaunches))
                }
            }
            is NetworkResponse.ServerError -> {
                emit(Resource.Error(dbLaunches, launches.body))
            }
            is NetworkResponse.NetworkError -> {
                emit(Resource.Error(dbLaunches, launches.error))
            }
        }
    }
        .flowOn(Dispatchers.IO)

    suspend fun flowUpcomingLaunches(currentTime: Long, limit: Int) = flow<Resource<List<DbLaunch>>> {
        emit(Resource.Loading)
        val dbLaunches = launchDao.getUpcomingLaunches(currentTime, limit)
        emit(Resource.Success(dbLaunches))

        when (val apiLaunches = launchesService.getUpcomingLaunches().await()) {
            is NetworkResponse.Success -> {
                saveApiLaunches(apiLaunches.body)
                val launches = launchDao.getUpcomingLaunches(currentTime, limit)
                if (launches != dbLaunches)
                    emit(Resource.Success(launches))
            }
            is NetworkResponse.ServerError -> emit(Resource.Error(dbLaunches, apiLaunches.body))

            is NetworkResponse.NetworkError -> emit(Resource.Error(dbLaunches, apiLaunches.error))
        }
    }
        .flowOn(Dispatchers.IO)

    suspend fun getNextLaunch(currentTime: Long): Resource<DbLaunch> = withContext(Dispatchers.IO) {
        val launch = executeWithRetry { launchesService.getNextLaunch().await() }
        when (launch) {
            is NetworkResponse.Success -> {
                saveApiLaunch(launch()!!)
                Resource.Success(launchDao.getNextLaunch(currentTime)!!) // Launch is definitely present because we just saved it
            }
            is NetworkResponse.ServerError -> {
                Resource.Error(launchDao.getNextLaunch(currentTime), launch.body)
            }
            is NetworkResponse.NetworkError -> {
                Resource.Error(launchDao.getNextLaunch(currentTime), launch.error)
            }
        }
    }

    suspend fun flowNextLaunch(timeAtStartOfDay: Long) = flow<Resource<LaunchMinimal>> {
        emit(Resource.Loading)
        val dbLaunch = launchDao.getNextLaunchMinimal(timeAtStartOfDay)
        if (dbLaunch != null) {
            emit(Resource.Success(dbLaunch))
        }
        when (val apiLaunch = launchesService.getNextLaunch().await()) {
            is NetworkResponse.Success -> {
                saveApiLaunch(apiLaunch.body)
                val launch = launchDao.getNextLaunchMinimal(timeAtStartOfDay)!!
                if (launch != dbLaunch) {
                    emit(Resource.Success(launch))
                }
            }
            is NetworkResponse.ServerError -> emit(Resource.Error(dbLaunch, apiLaunch.body))

            is NetworkResponse.NetworkError -> emit(Resource.Error(dbLaunch, apiLaunch.error))
        }
    }
        .flowOn(Dispatchers.IO)

    suspend fun flowPastLaunches(currentTime: Long, limit: Int) = flow<Resource<List<DbLaunch>>> {
        emit(Resource.Loading)

        val dbLaunches = launchDao.getPastLaunches(currentTime, limit)

        when (val apiLaunches = launchesService.getPastLaunches().await()) {
            is NetworkResponse.Success -> {
                saveApiLaunches(apiLaunches.body)
                val launches = launchDao.getPastLaunches(currentTime, limit)
                if (launches != dbLaunches) {
                    emit(Resource.Success(launches))
                }
            }
            is NetworkResponse.ServerError -> emit(Resource.Error(dbLaunches, apiLaunches.body))
            is NetworkResponse.NetworkError -> emit(Resource.Error(dbLaunches, apiLaunches.error))
        }
    }
        .flowOn(Dispatchers.IO)

    suspend fun flowLaunch(flightNumber: Int) = flow<Resource<LaunchMinimal>> {
        emit(Resource.Loading)

        val launch = launchDao.getLaunchMinimal(flightNumber)
        if (launch != null) emit(Resource.Success(launch))

        when (val launchResponse = executeWithRetry { launchesService.getLaunch(flightNumber).await() }) {
            is NetworkResponse.Success -> {
                saveApiLaunch(launchResponse()!!)
                val dbLaunch = launchDao.getLaunchMinimal(flightNumber)!!
                emit(Resource.Success(dbLaunch))
            }
            is NetworkResponse.ServerError -> {
                emit(Resource.Error(launch, launchResponse.body))
            }
            is NetworkResponse.NetworkError -> {
                emit(Resource.Error(launch, launchResponse.error))
            }
        }
    }
        .flowOn(Dispatchers.IO)

    suspend fun flowLaunchStats(flightNumber: Int) = flow<Resource<LaunchStats>> {
        val launchStats = launchDao.getLaunchStats(flightNumber)
        launchStats?.let { stats ->
            emit(Resource.Success(stats))
        } ?: emit(Resource.Error(null, null))
    }
        .flowOn(Dispatchers.IO)

    suspend fun flowLaunchPictures(flightNumber: Int) = flow<Resource<LaunchPictures>> {
        val pictures = launchDao.getLaunchPictures(flightNumber)
        pictures?.let { list ->
            emit(Resource.Success(list))
        } ?: emit(Resource.Error(null, null))
    }
        .flowOn(Dispatchers.IO)

    suspend fun flowLaunchesForLaunchPad(siteId: String, maxTimeStamp: Long, minTimeStamp: Long) = flow<Resource<List<LaunchMinimal>>> {
        emit(Resource.Loading)

        val dbLaunches = launchDao.getLaunchesForLaunchPad(siteId, maxTimeStamp, minTimeStamp)
        if (dbLaunches.isNotEmpty()) {
            emit(Resource.Success(dbLaunches))
        }

        when (val apiLaunches = launchesService.getAllLaunches(siteId = siteId).await()) {
            is NetworkResponse.Success -> {
                saveApiLaunches(apiLaunches.body)
                val launches = launchDao.getLaunchesForLaunchPad(siteId, maxTimeStamp, minTimeStamp)
                if (launches != dbLaunches)
                    emit(Resource.Success(launches))
            }
            is NetworkResponse.ServerError -> emit(Resource.Error(dbLaunches, apiLaunches.body))

            is NetworkResponse.NetworkError -> emit(Resource.Error(dbLaunches, apiLaunches.error))
        }
    }
        .flowOn(Dispatchers.IO)

    suspend fun syncLaunches(): Resource<Unit> {

        val apiLaunches = executeWithRetry {
            launchesService.getAllLaunches().await()
        }

        return when (apiLaunches) {
            is NetworkResponse.Success -> {
                saveApiLaunches(apiLaunches.body)
                Resource.Success(Unit)
            }
            is NetworkResponse.ServerError -> Resource.Error(Unit, null)
            is NetworkResponse.NetworkError -> Resource.Error(Unit, null)
        }
    }

    suspend fun getNextLaunchFromDatabase(currentTime: Long): DbLaunch? {
        return launchDao.getNextLaunch(currentTime)
    }

    suspend fun getLaunchesInTimeRangeFromDatabase(start: Long, end: Long, limit: Int): List<DbLaunch> {
        return launchDao.getLaunchesInRange(start, end, limit)
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