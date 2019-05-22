package com.haroldadmin.moonshot_repository

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.capsule.CapsuleService
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.core.CoresService
import com.haroldadmin.spacex_api_wrapper.dragon.DragonsService
import com.haroldadmin.spacex_api_wrapper.history.HistoryService
import com.haroldadmin.spacex_api_wrapper.info.InfoService
import com.haroldadmin.spacex_api_wrapper.landingpads.LandingPadsService
import com.haroldadmin.spacex_api_wrapper.launches.Launch
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPadService
import com.haroldadmin.spacex_api_wrapper.mission.MissionService
import com.haroldadmin.spacex_api_wrapper.payload.PayloadsService
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteDataSource (
    private val capsuleService: CapsuleService,
    private val coresService: CoresService,
    private val dragonsService: DragonsService,
    private val historyService: HistoryService,
    private val infoService: InfoService,
    private val landingPadsService: LandingPadsService,
    private val launchesService: LaunchesService,
    private val launchPadService: LaunchPadService,
    private val missionService: MissionService,
    private val payloadsService: PayloadsService,
    private val rocketsService: RocketsService
) {

    suspend fun getAllLaunches(): NetworkResponse<List<Launch>, ErrorResponse> = withContext(Dispatchers.IO) {
        launchesService.getAllLaunches().await()
    }

    suspend fun getLaunch(flightNumber: Int): NetworkResponse<Launch, ErrorResponse> = withContext(Dispatchers.IO) {
        launchesService.getLaunch(flightNumber).await()
    }

    suspend fun getUpcomingLaunches(): NetworkResponse<List<Launch>, ErrorResponse> = withContext(Dispatchers.IO) {
        launchesService.getUpcomingLaunches().await()
    }

    suspend fun getPastLaunches(): NetworkResponse<List<Launch>, ErrorResponse> = withContext(Dispatchers.IO) {
        launchesService.getPastLaunches().await()
    }
}