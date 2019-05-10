package com.haroldadmin.spacex_api_wrapper.launches

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LaunchesService {

    @GET("/launches")
    fun getAllLaunches(
        @Query("flight_id") flightId: String? = null,
        @Query("start") start: String? = null,
        @Query("end") end: String? = null,
        @Query("land_success") landSucess: Boolean? = null,
        @Query("site_id") siteId: String? = null,
        @Query("customer") customer: String? = null,
        @Query("nationality") nationality: String? = null,
        @Query("launch_success") launchSuccess: Boolean? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: String = "original_launch",
        @Query("order") order: String = "desc"
    ): Deferred<NetworkResponse<List<Launch>, ErrorResponse>>

    @GET("/launches/{flightNumber}")
    fun getLaunch(@Path("flightNumber") flightNumber: Int): Deferred<NetworkResponse<Launch, ErrorResponse>>

    @GET("/launches/past")
    fun getPastLaunches(
        @Query("flight_id") flightId: String? = null,
        @Query("start") start: String? = null,
        @Query("end") end: String? = null,
        @Query("land_success") landSucess: Boolean? = null,
        @Query("site_id") siteId: String? = null,
        @Query("customer") customer: String? = null,
        @Query("nationality") nationality: String? = null,
        @Query("launch_success") launchSuccess: Boolean? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: String = "original_launch",
        @Query("order") order: String = "desc"
    ): Deferred<NetworkResponse<List<Launch>, ErrorResponse>>

    @GET("/launches/upcoming")
    fun getUpcomingLaunches(
        @Query("flight_id") flightId: String? = null,
        @Query("start") start: String? = null,
        @Query("end") end: String? = null,
        @Query("land_success") landSucess: Boolean? = null,
        @Query("site_id") siteId: String? = null,
        @Query("customer") customer: String? = null,
        @Query("nationality") nationality: String? = null,
        @Query("launch_success") launchSuccess: Boolean? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: String = "original_launch",
        @Query("order") order: String = "asc"
    ): Deferred<NetworkResponse<List<Launch>, ErrorResponse>>

    @GET("/launches/latest")
    fun getLatestLaunch(): Deferred<NetworkResponse<Launch, ErrorResponse>>

    @GET("/launches/next")
    fun getNextLaunch(): Deferred<NetworkResponse<Launch, ErrorResponse>>
}