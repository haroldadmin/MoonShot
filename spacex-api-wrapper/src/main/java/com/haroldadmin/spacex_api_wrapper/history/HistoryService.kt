package com.haroldadmin.spacex_api_wrapper.history

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HistoryService {

    @GET("history")
    fun getAllHistoricalEvents(
        @Query("start") start: String? = null,
        @Query("end") end: String? = null,
        @Query("flight_number") flightNumber: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: String = "flight_number",
        @Query("order") order: String = "desc"
    ): Deferred<NetworkResponse<List<HistoricalEvent>, ErrorResponse>>

    @GET("history/{id}")
    fun getHistoricalEvent(@Path("id") id: Int): Deferred<NetworkResponse<HistoricalEvent, ErrorResponse>>
}