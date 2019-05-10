package com.haroldadmin.spacex_api_wrapper.dragon

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DragonsService {

    @GET("/dragons")
    fun getAllDragons(
        @Query("limit") limit: Int? = null
    ): Deferred<NetworkResponse<List<Dragon>, ErrorResponse>>

    @GET("/dragons/{id}")
    fun getDragon(@Path("id") id: String): Deferred<NetworkResponse<Dragon, ErrorResponse>>
}