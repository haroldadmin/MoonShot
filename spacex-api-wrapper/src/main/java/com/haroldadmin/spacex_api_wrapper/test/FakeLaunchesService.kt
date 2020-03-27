package com.haroldadmin.spacex_api_wrapper.test

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import com.haroldadmin.spacex_api_wrapper.launches.Launch
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import java.io.IOException
import javax.inject.Inject

class FakeLaunchesService @Inject constructor() : LaunchesService {

    private val launches = mutableListOf<Launch>()
    private var expectedResponseType: FakeResponseType = FakeResponseType.Success

    fun seedWith(vararg launch: Launch) {
        launches.addAll(launch)
    }

    fun expect(responseType: FakeResponseType) {
        expectedResponseType = responseType
    }

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
        return CompletableDeferred(expectedResponseType.toNetworkResponse(launches))
    }

    override fun getLaunch(flightNumber: Int): Deferred<NetworkResponse<Launch, ErrorResponse>> {
        val responseData = launches.find { it.flightNumber == flightNumber } ?: error {
            "Requested Launch (flight number: $flightNumber) not found in seed data"
        }
        return CompletableDeferred(expectedResponseType.toNetworkResponse(responseData))
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
        val responseData = launches.filter { it.upcoming == false }
        return CompletableDeferred(expectedResponseType.toNetworkResponse(responseData))
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
        val responseData = launches.filter { it.upcoming == true }
        return CompletableDeferred(expectedResponseType.toNetworkResponse(responseData))
    }

    override fun getLatestLaunch(): Deferred<NetworkResponse<Launch, ErrorResponse>> {
        val responseData = launches.filter { it.upcoming == false }.maxBy { it.flightNumber } ?: error {
            "FakeLaunchesService not seeded with an upcoming launch"
        }
        return CompletableDeferred(expectedResponseType.toNetworkResponse(responseData))
    }

    override fun getNextLaunch(): Deferred<NetworkResponse<Launch, ErrorResponse>> {
        val responseData = launches.filter { it.upcoming == true }.minBy { it.flightNumber } ?: error {
            "FakeLaunchesService not seeded with an upcoming launch"
        }
        return CompletableDeferred(expectedResponseType.toNetworkResponse(responseData))
    }
}