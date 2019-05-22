package com.haroldadmin.spacex_api_wrapper.rocket

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RocketsService {

    @GET("rockets")
    fun getAllRockets(
        @Query("limit") limit: Int? = null
    ): Deferred<NetworkResponse<List<Rocket>, ErrorResponse>>

    @GET("rockets/{rocketId}")
    fun getRocket(@Path("rocketId") rocketId: String): Deferred<NetworkResponse<Rocket, ErrorResponse>>
}