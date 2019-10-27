@file:Suppress("DeferredIsResult")

package com.haroldadmin.moonshotRepository.launches

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshotRepository.ExpectedResponse
import com.haroldadmin.spacex_api_wrapper.SampleApiData
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.launches.Launch
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import com.haroldadmin.moonshotRepository.ioErrorResponse
import com.haroldadmin.moonshotRepository.successfulResponse
import com.haroldadmin.moonshotRepository.serverErrorResponse
import com.haroldadmin.moonshotRepository.toDeferred
import kotlinx.coroutines.Deferred

internal class FakeLaunchesService : LaunchesService {

    var expectedResponse: ExpectedResponse = ExpectedResponse.Success

    override fun getAllLaunches(
        flightId: String?,
        start: String?,
        end: String?,
        landSucess: Boolean?,
        siteId: String?,
        customer: String?,
        nationality: String?,
        launchSuccess: Boolean?,
        limit: Int?,
        sort: String,
        order: String
    ): Deferred<NetworkResponse<List<Launch>, ErrorResponse>> {
        return when (expectedResponse) {
            ExpectedResponse.Success -> successfulResponse {
                SampleApiData.Launches.all().take(10).toList()
            }.toDeferred()

            ExpectedResponse.NetworkError -> ioErrorResponse().toDeferred()

            ExpectedResponse.ServerError -> serverErrorResponse {
                ErrorResponse("Not found")
            }.toDeferred()
        }
    }

    override fun getLaunch(flightNumber: Int): Deferred<NetworkResponse<Launch, ErrorResponse>> {
        return when (expectedResponse) {
            ExpectedResponse.Success -> successfulResponse {
                SampleApiData.Launches.one(flightNumber = flightNumber)
            }.toDeferred()

            ExpectedResponse.NetworkError -> ioErrorResponse().toDeferred()

            ExpectedResponse.ServerError -> serverErrorResponse {
                ErrorResponse("Not found")
            }.toDeferred()
        }
    }

    override fun getPastLaunches(
        flightId: String?,
        start: String?,
        end: String?,
        landSucess: Boolean?,
        siteId: String?,
        customer: String?,
        nationality: String?,
        launchSuccess: Boolean?,
        limit: Int?,
        sort: String,
        order: String
    ): Deferred<NetworkResponse<List<Launch>, ErrorResponse>> {
        return when (expectedResponse) {
            ExpectedResponse.Success -> successfulResponse {
                SampleApiData.Launches
                    .all(isUpcomingGenerator = { false })
                    .take(10)
                    .toList()
            }.toDeferred()

            ExpectedResponse.NetworkError -> ioErrorResponse().toDeferred()

            ExpectedResponse.ServerError -> serverErrorResponse {
                ErrorResponse("Not found")
            }.toDeferred()
        }
    }

    override fun getUpcomingLaunches(
        flightId: String?,
        start: String?,
        end: String?,
        landSucess: Boolean?,
        siteId: String?,
        customer: String?,
        nationality: String?,
        launchSuccess: Boolean?,
        limit: Int?,
        sort: String,
        order: String
    ): Deferred<NetworkResponse<List<Launch>, ErrorResponse>> {
        return when (expectedResponse) {
            ExpectedResponse.Success -> successfulResponse {
                SampleApiData.Launches
                    .all(isUpcomingGenerator = { true })
                    .take(10)
                    .toList()
            }.toDeferred()

            ExpectedResponse.NetworkError -> ioErrorResponse().toDeferred()

            ExpectedResponse.ServerError -> serverErrorResponse {
                ErrorResponse("Not found")
            }.toDeferred()
        }
    }

    override fun getLatestLaunch(): Deferred<NetworkResponse<Launch, ErrorResponse>> {
        return when (expectedResponse) {
            ExpectedResponse.Success -> successfulResponse {
                SampleApiData.Launches.one(isUpcoming = false)
            }.toDeferred()

            ExpectedResponse.NetworkError -> ioErrorResponse().toDeferred()

            ExpectedResponse.ServerError -> serverErrorResponse {
                ErrorResponse("Not found")
            }.toDeferred()
        }
    }

    override fun getNextLaunch(): Deferred<NetworkResponse<Launch, ErrorResponse>> {
        return when (expectedResponse) {
            ExpectedResponse.Success -> successfulResponse {
                SampleApiData.Launches.one(isUpcoming = true)
            }.toDeferred()

            ExpectedResponse.NetworkError -> ioErrorResponse().toDeferred()

            ExpectedResponse.ServerError -> serverErrorResponse {
                ErrorResponse("Not found")
            }.toDeferred()
        }
    }
}