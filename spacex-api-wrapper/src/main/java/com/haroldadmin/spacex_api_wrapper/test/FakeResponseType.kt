package com.haroldadmin.spacex_api_wrapper.test

import com.haroldadmin.cnradapter.NetworkResponse
import java.io.IOException

enum class FakeResponseType {
    ServerError,
    NetworkError,
    Success
}

internal fun <T: Any, U: Any> FakeResponseType.toNetworkResponse(
    successResponse: T,
    errorResponse: U? = null,
    errorCode: Int = 404
): NetworkResponse<T, U> {
    return when (this) {
        FakeResponseType.ServerError -> NetworkResponse.ServerError(errorResponse, errorCode)
        FakeResponseType.NetworkError -> NetworkResponse.NetworkError(IOException())
        FakeResponseType.Success -> NetworkResponse.Success(successResponse)
    }
}
