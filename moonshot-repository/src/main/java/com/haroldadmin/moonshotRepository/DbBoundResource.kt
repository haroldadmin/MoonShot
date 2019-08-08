package com.haroldadmin.moonshotRepository

import com.haroldadmin.moonshot.core.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

abstract class DbBoundResource<T: Any> {
    abstract suspend fun fetchFromDb(): T?
    abstract suspend fun validateData(data: T?): Boolean

    @ExperimentalCoroutinesApi
    suspend fun flow(): Flow<Resource<T>> = flow {
        val cached = fetchFromDb()
        if (validateData(cached)) {
            emit(Resource.Success(cached!!))
        } else {
            emit(Resource.Error(data = null, error = null))
        }
    }.onStart { emit(Resource.Loading) }
}

@ExperimentalCoroutinesApi
suspend inline fun <T: Any> dbBoundResource(
    crossinline dbFetcher: suspend () -> T?,
    crossinline validator: suspend (T?) -> Boolean
): Flow<Resource<T>> {
    val resource = object : DbBoundResource<T>() {
        override suspend fun fetchFromDb(): T? {
            return dbFetcher()
        }

        override suspend fun validateData(data: T?): Boolean {
            return validator(data)
        }
    }
    return resource.flow()
}