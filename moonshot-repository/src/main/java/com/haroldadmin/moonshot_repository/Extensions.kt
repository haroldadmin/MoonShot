package com.haroldadmin.moonshot_repository

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.core.Resource

internal fun <T: Any, U: Any> NetworkResponse<T, U>.toResource(): Resource<T> {
    return when (this) {
        is NetworkResponse.Success -> Resource.Success(this.body)
        is NetworkResponse.ServerError -> Resource.Error<T, U>(null, this.body!!)
        is NetworkResponse.NetworkError -> Resource.Error(null, this.error)
    }
}