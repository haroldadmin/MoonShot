package com.haroldadmin.spacex_api_wrapper.capsule

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Date

interface CapsuleService {
    @GET("capsules")
    fun getAllCapsules(
        @Query("status") status: String? = null,
        @Query("original_launch") originalLaunch: Date? = null,
        @Query("missions") mission: String? = null,
        @Query("landings") landings: Int? = null,
        @Query("type") type: String? = null,
        @Query("reuse_count") reuseCount: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: String = "original_launch",
        @Query("order") order: String = "desc"
    ): Deferred<NetworkResponse<List<Capsule>, ErrorResponse>>

    @GET("/capsules/{capsuleSerial}")
    fun getCapsule(@Path("capsuleSerial") serial: String): Deferred<NetworkResponse<Capsule, ErrorResponse>>

    @GET("/capsules/upcoming")
    fun getUpcomingCapsules(
        @Query("status") status: String? = null,
        @Query("original_launch") originalLaunch: Date? = null,
        @Query("missions") mission: String? = null,
        @Query("landings") landings: Int? = null,
        @Query("type") type: String? = null,
        @Query("reuse_count") reuseCount: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: String = "original_launch",
        @Query("order") order: String = "desc"
    ): Deferred<NetworkResponse<List<Capsule>, ErrorResponse>>

    @GET("/capsules/past")
    fun getPastCapsules(
        @Query("status") status: String? = null,
        @Query("original_launch") originalLaunch: Date? = null,
        @Query("missions") mission: String? = null,
        @Query("landings") landings: Int? = null,
        @Query("type") type: String? = null,
        @Query("reuse_count") reuseCount: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: String = "original_launch",
        @Query("order") order: String = "desc"
    ): Deferred<NetworkResponse<List<Capsule>, ErrorResponse>>
}