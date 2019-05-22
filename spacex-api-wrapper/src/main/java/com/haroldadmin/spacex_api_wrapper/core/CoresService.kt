package com.haroldadmin.spacex_api_wrapper.core

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Date

interface CoresService {

    @GET("cores")
    fun getAllCores(
        @Query("block") block: Int? = null,
        @Query("status") status: String? = null,
        @Query("original_launch") originalLaunch: Date? = null,
        @Query("missions") missions: String? = null,
        @Query("reuse_count") reuseCount: Int? = null,
        @Query("rtls_attempts") rtlsAttempts: Int? = null,
        @Query("rtls_landings") rtlsLandings: Int? = null,
        @Query("asds_attempts") asdsAttempts: Int? = null,
        @Query("asds_landings") asdsLandings: Int? = null,
        @Query("water_landing") waterLanding: Boolean? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: String = "original_launch",
        @Query("order") order: String = "desc"
        ): Deferred<NetworkResponse<List<Core>, ErrorResponse>>

    @GET("cores/{coreSerial}")
    fun getCore(@Path("coreSerial") serial: String): Deferred<NetworkResponse<Core, ErrorResponse>>

    @GET("cores/upcoming")
    fun getUpcomingCores(
        @Query("block") block: Int? = null,
        @Query("status") status: String? = null,
        @Query("original_launch") originalLaunch: Date? = null,
        @Query("missions") missions: String? = null,
        @Query("reuse_count") reuseCount: Int? = null,
        @Query("rtls_attempts") rtlsAttempts: Int? = null,
        @Query("rtls_landings") rtlsLandings: Int? = null,
        @Query("asds_attempts") asdsAttempts: Int? = null,
        @Query("asds_landings") asdsLandings: Int? = null,
        @Query("water_landing") waterLanding: Boolean? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: String = "original_launch",
        @Query("order") order: String = "asc"
    ): Deferred<NetworkResponse<List<Core>, ErrorResponse>>

    @GET("cores/past")
    fun getPastCores(
        @Query("block") block: Int? = null,
        @Query("status") status: String? = null,
        @Query("original_launch") originalLaunch: Date? = null,
        @Query("missions") missions: String? = null,
        @Query("reuse_count") reuseCount: Int? = null,
        @Query("rtls_attempts") rtlsAttempts: Int? = null,
        @Query("rtls_landings") rtlsLandings: Int? = null,
        @Query("asds_attempts") asdsAttempts: Int? = null,
        @Query("asds_landings") asdsLandings: Int? = null,
        @Query("water_landing") waterLanding: Boolean? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: String = "original_launch",
        @Query("order") order: String = "asc"
    ): Deferred<NetworkResponse<List<Core>, ErrorResponse>>
}