package com.haroldadmin.moonshotRepository.rockets

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshotRepository.ExpectedResponse
import com.haroldadmin.moonshotRepository.ioErrorResponse
import com.haroldadmin.moonshotRepository.serverErrorResponse
import com.haroldadmin.moonshotRepository.successfulResponse
import com.haroldadmin.moonshotRepository.toDeferred
import com.haroldadmin.spacex_api_wrapper.SampleApiData
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.rocket.Rocket
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import kotlinx.coroutines.Deferred

internal class FakeRocketsService : RocketsService {

    var expectedType: ExpectedResponse = ExpectedResponse.Success

    override fun getAllRockets(limit: Int?): Deferred<NetworkResponse<List<Rocket>, ErrorResponse>> {
        return when (expectedType) {
            ExpectedResponse.Success -> successfulResponse {
                SampleApiData.Rockets.all().take(limit ?: 10).toList()
            }.toDeferred()

            ExpectedResponse.NetworkError -> ioErrorResponse().toDeferred()

            ExpectedResponse.ServerError -> serverErrorResponse {
                ErrorResponse("Not found")
            }.toDeferred()
        }
    }

    override fun getRocket(rocketId: String): Deferred<NetworkResponse<Rocket, ErrorResponse>> {
        return when (expectedType) {
            ExpectedResponse.Success -> successfulResponse {
                SampleApiData.Rockets.one(rocketId = rocketId)
            }.toDeferred()

            ExpectedResponse.NetworkError -> ioErrorResponse().toDeferred()

            ExpectedResponse.ServerError -> serverErrorResponse {
                ErrorResponse("Not found")
            }.toDeferred()
        }
    }
}