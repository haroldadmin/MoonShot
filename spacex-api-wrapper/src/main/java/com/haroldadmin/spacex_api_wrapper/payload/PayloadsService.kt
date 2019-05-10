package com.haroldadmin.spacex_api_wrapper.payload

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface PayloadsService {

    @GET("/payloads")
    fun getAllPayloads(): Deferred<NetworkResponse<List<Payload>, ErrorResponse>>

    @GET("/payloads/{payloadId}")
    fun getPayload(@Path("payloadId") payloadId: String): Deferred<NetworkResponse<Payload, ErrorResponse>>
}