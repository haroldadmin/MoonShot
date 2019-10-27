@file:Suppress("DeferredIsResult")

package com.haroldadmin.moonshotRepository.launchPad

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshotRepository.ExpectedResponse
import com.haroldadmin.moonshotRepository.ioErrorResponse
import com.haroldadmin.moonshotRepository.serverErrorResponse
import com.haroldadmin.moonshotRepository.successfulResponse
import com.haroldadmin.moonshotRepository.toDeferred
import com.haroldadmin.spacex_api_wrapper.SampleApiData
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPad
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPadService
import kotlinx.coroutines.Deferred

internal class FakeLaunchPadService : LaunchPadService {

    var expectedResponse: ExpectedResponse = ExpectedResponse.Success

    override fun getAllLaunchPads(limit: Int?): Deferred<NetworkResponse<List<LaunchPad>, ErrorResponse>> {
        return when (expectedResponse) {
            ExpectedResponse.Success -> successfulResponse {
                Int
                SampleApiData.LaunchPads.many().take(limit ?: 10).toList()
            }.toDeferred()

            ExpectedResponse.NetworkError -> ioErrorResponse().toDeferred()

            ExpectedResponse.ServerError -> serverErrorResponse {
                ErrorResponse("Not found")
            }.toDeferred()
        }
    }

    override fun getLaunchPad(siteId: String): Deferred<NetworkResponse<LaunchPad, ErrorResponse>> {
        return when (expectedResponse) {
            ExpectedResponse.Success -> successfulResponse {
                SampleApiData.LaunchPads.one(siteId = siteId)
            }.toDeferred()

            ExpectedResponse.NetworkError -> ioErrorResponse().toDeferred()

            ExpectedResponse.ServerError -> serverErrorResponse {
                ErrorResponse("Not found")
            }.toDeferred()
        }
    }
}