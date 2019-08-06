package com.haroldadmin.moonshotRepository.launch

import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.CoreSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.FirstStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.SecondStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.payload.Payload
import com.haroldadmin.moonshotRepository.mappers.toDbCoreSummary
import com.haroldadmin.moonshotRepository.mappers.toDbFirstStageSummary
import com.haroldadmin.moonshotRepository.mappers.toDbLaunch
import com.haroldadmin.moonshotRepository.mappers.toDbPayload
import com.haroldadmin.moonshotRepository.mappers.toDbRocketSummary
import com.haroldadmin.moonshotRepository.mappers.toDbSecondStageSummary
import com.haroldadmin.spacex_api_wrapper.launches.Launch
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class LaunchesUseCase(
    protected val launchesDao: LaunchDao,
    protected val launchesService: LaunchesService
) {

    protected suspend fun persistLaunches(apiLaunches: List<Launch>) {
        withContext(Dispatchers.Default) {
            val dbLaunches = apiLaunches.map { it.toDbLaunch() }

            val rocketSummaries: List<RocketSummary> =
                apiLaunches.map { launch -> launch.rocket.toDbRocketSummary(launch.flightNumber) }

            val firstStageSummaries: List<FirstStageSummary> =
                apiLaunches.map { launch -> launch.rocket.firstStage.toDbFirstStageSummary(launch.flightNumber) }

            val secondStageSummaries: List<SecondStageSummary> =
                apiLaunches.map { launch -> launch.rocket.secondState.toDbSecondStageSummary(launch.flightNumber) }

            val coreSummaries: List<CoreSummary> = apiLaunches.flatMap { launch ->
                launch.rocket.firstStage.cores.map { core -> core.toDbCoreSummary(launch.flightNumber) }
            }

            val payloads: List<Payload> = apiLaunches.flatMap { launch ->
                launch.rocket.secondState.payloads.map { payload -> payload.toDbPayload(launch.flightNumber) }
            }

            withContext(Dispatchers.IO) {
                launchesDao.saveLaunchesWithSummaries(
                    dbLaunches,
                    rocketSummaries,
                    firstStageSummaries,
                    coreSummaries,
                    secondStageSummaries,
                    payloads
                )
            }
        }
    }
}