package com.haroldadmin.spacex_api_wrapper.info

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface InfoService {

    @GET("info")
    fun getSpacexInfo(): Deferred<NetworkResponse<CompanyInfo, ErrorResponse>>
}