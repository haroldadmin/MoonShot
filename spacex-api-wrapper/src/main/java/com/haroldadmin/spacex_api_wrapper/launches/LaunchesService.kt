package com.haroldadmin.spacex_api_wrapper.launches

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface LaunchesService {

    @GET("/launches")
    fun getAllLaunches(): Deferred<NetworkResponse<List<Launch>, ErrorResponse>>

    @GET("/launches/{flightNumber}")
    fun getLaunch(@Path("flightNumber") flightNumber: Int): Deferred<NetworkResponse<Launch, ErrorResponse>>

    @GET("/launches/past")
    fun getPastLaunches(): Deferred<NetworkResponse<List<Launch>, ErrorResponse>>

    @GET("/launches/upcoming")
    fun getUpcomingLaunches(): Deferred<NetworkResponse<List<Launch>, ErrorResponse>>

    @GET("/launches/latest")
    fun getLatestLaunch(): Deferred<NetworkResponse<Launch, ErrorResponse>>

    @GET("/launches/next")
    fun getNextLaunch(): Deferred<NetworkResponse<Launch, ErrorResponse>>
}