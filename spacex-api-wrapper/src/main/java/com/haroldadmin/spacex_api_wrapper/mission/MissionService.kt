package com.haroldadmin.spacex_api_wrapper.mission

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface MissionService {

    @GET("missions")
    fun getAllMissions(): Deferred<NetworkResponse<List<Mission>, ErrorResponse>>

    @GET("missions/{missionId}")
    fun getMission(@Path("missionId") missionId: String): Deferred<NetworkResponse<Mission, ErrorResponse>>
}