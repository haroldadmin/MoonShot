package com.haroldadmin.spacex_api_wrapper.landingpads

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LandingPadsService {

    @GET("landpads")
    fun getAllLandingPads(
        @Query("limit") limit: Int? = null
    ): Deferred<NetworkResponse<List<LandingPad>, ErrorResponse>>

    @GET("landpads/{id}")
    fun getLandingPad(@Path("id") id: Int): Deferred<NetworkResponse<LandingPad, ErrorResponse>>
}