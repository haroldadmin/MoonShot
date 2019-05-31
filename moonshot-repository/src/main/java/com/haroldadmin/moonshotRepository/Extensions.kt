package com.haroldadmin.moonshotRepository

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.core.Resource
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

internal fun <T : Any, U : Any> NetworkResponse<T, U>.toResource(): Resource<T> {
    return when (this) {
        is NetworkResponse.Success -> Resource.Success(this.body)
        is NetworkResponse.ServerError -> Resource.Error<T, U>(null, this.body!!)
        is NetworkResponse.NetworkError -> Resource.Error(null, this.error)
    }
}

@FlowPreview
fun <T> Flow<T>.buffer(size: Int = 0): Flow<T> = flow {
    coroutineScope {
        val channel = produce(capacity = size) {
            collect { send(it) }
        }
        channel.consumeEach { emit(it) }
    }
}