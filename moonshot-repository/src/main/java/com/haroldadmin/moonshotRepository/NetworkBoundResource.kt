package com.haroldadmin.moonshotRepository

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.core.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

abstract class NetworkBoundResource<T: Any, U: Any, V: Any> {
    abstract suspend fun getFromDatabase(isRefreshed: Boolean): T
    abstract suspend fun validateCache(cachedData: T): Boolean
    abstract suspend fun getFromApi(): NetworkResponse<U, V>
    abstract suspend fun persistData(apiData: U)

    fun flow(): Flow<Resource<T>> {
        return flow {
            val cachedData = getFromDatabase(isRefreshed = false)
            if (validateCache(cachedData)) {
                emit(Resource.Success(cachedData, isCached = true))
            }

            when (val apiResponse = getFromApi()) {
                is NetworkResponse.Success -> {
                    persistData(apiResponse.body)
                    val refreshedData = getFromDatabase(isRefreshed = true)
                    emit(Resource.Success(refreshedData, isCached = false))
                }
                is NetworkResponse.ServerError -> {
                    val error = apiResponse.body
                    emit(Resource.Error(cachedData, error))
                }
                is NetworkResponse.NetworkError -> {
                    val error = apiResponse.error
                    emit(Resource.Error(cachedData, error))
                }
            }
        }
    }
}

inline fun <T: Any, U: Any, V: Any> networkBoundFlow(
    crossinline dbFetcher: suspend (Boolean) -> T,
    crossinline apiFetcher: suspend () -> NetworkResponse<U, V>,
    crossinline cacheValidator: suspend (T) -> Boolean,
    crossinline dataPersister: suspend (U) -> Unit
    ): Flow<Resource<T>> {
    val resource = object : NetworkBoundResource<T, U, V>() {
        override suspend fun getFromDatabase(isRefreshed: Boolean): T {
            return dbFetcher(isRefreshed)
        }

        override suspend fun validateCache(cachedData: T): Boolean {
            return cacheValidator(cachedData)
        }

        override suspend fun getFromApi(): NetworkResponse<U, V> {
            return apiFetcher()
        }

        override suspend fun persistData(apiData: U) {
            dataPersister(apiData)
        }
    }
    return resource.flow().onStart { emit(Resource.Loading) }
}