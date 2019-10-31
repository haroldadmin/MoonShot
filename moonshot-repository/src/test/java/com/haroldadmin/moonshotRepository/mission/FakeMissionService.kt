package com.haroldadmin.moonshotRepository.mission

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshotRepository.ExpectedResponse
import com.haroldadmin.moonshotRepository.ioErrorResponse
import com.haroldadmin.moonshotRepository.serverErrorResponse
import com.haroldadmin.moonshotRepository.successfulResponse
import com.haroldadmin.moonshotRepository.toDeferred
import com.haroldadmin.spacex_api_wrapper.SampleApiData
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.mission.Mission
import com.haroldadmin.spacex_api_wrapper.mission.MissionService
import kotlinx.coroutines.Deferred

internal class FakeMissionService : MissionService {

    var expectedResponse: ExpectedResponse = ExpectedResponse.Success

    override fun getAllMissions(): Deferred<NetworkResponse<List<Mission>, ErrorResponse>> {
        return when (expectedResponse) {
            ExpectedResponse.Success -> successfulResponse {
                SampleApiData.Missions.many().take(10).toList()
            }.toDeferred()
            ExpectedResponse.NetworkError -> ioErrorResponse().toDeferred()
            ExpectedResponse.ServerError -> serverErrorResponse {
                ErrorResponse("Not found")
            }.toDeferred()
        }
    }

    override fun getMission(missionId: String): Deferred<NetworkResponse<Mission, ErrorResponse>> {
        return when (expectedResponse) {
            ExpectedResponse.Success -> successfulResponse {
                SampleApiData.Missions.one(id = missionId)
            }.toDeferred()
            ExpectedResponse.NetworkError -> ioErrorResponse().toDeferred()
            ExpectedResponse.ServerError -> serverErrorResponse {
                ErrorResponse("Not found")
            }.toDeferred()
        }
    }
}