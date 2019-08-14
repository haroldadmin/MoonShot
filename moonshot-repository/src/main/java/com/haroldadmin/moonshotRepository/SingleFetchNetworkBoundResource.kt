package com.haroldadmin.moonshotRepository

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.core.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

/**
 * A class that fetches data from the network just once, and then returns
 * cached data on subsequent requests
 */
abstract class SingleFetchNetworkBoundResource<T: Any, U: Any, V: Any>: NetworkBoundResource<T, U, V>() {

    private var hasDataBeenFetched: Boolean = false

    @ExperimentalCoroutinesApi
    override fun flow(): Flow<Resource<T>> {
        return flow {
            val cachedData = getFromDatabase(isRefreshed = hasDataBeenFetched)
            if (validateCache(cachedData)) {
                emit(Resource.Success(cachedData!!, isCached = true))
            }

            if (!hasDataBeenFetched) {
                println("[SingleFetchResource] Data has not been fetched yet")
                when (val apiResponse = getFromApi()) {
                    is NetworkResponse.Success -> {
                        persistData(apiResponse.body)
                        hasDataBeenFetched = true
                        val refreshedData = getFromDatabase(isRefreshed = true)!!
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
            } else {
                println("[SingleFetchResource] Data has already been fetched")
                cachedData?.let {
                    emit(Resource.Success(it, isCached = false))
                } ?: Resource.Error(cachedData, null)
            }
        }.onStart { emit(Resource.Loading) }
    }
}

@ExperimentalCoroutinesApi
inline fun <T : Any, U : Any, V : Any> singleFetchNetworkBoundFlow(
    crossinline dbFetcher: suspend (Boolean) -> T?,
    crossinline apiFetcher: suspend () -> NetworkResponse<U, V>,
    crossinline cacheValidator: suspend (T?) -> Boolean,
    crossinline dataPersister: suspend (U) -> Unit
): Flow<Resource<T>> {
    val resource = object : SingleFetchNetworkBoundResource<T, U, V>() {
        override suspend fun getFromDatabase(isRefreshed: Boolean): T? {
            return dbFetcher(isRefreshed)
        }

        override suspend fun validateCache(cachedData: T?): Boolean {
            return cacheValidator(cachedData)
        }

        override suspend fun getFromApi(): NetworkResponse<U, V> {
            return apiFetcher()
        }

        override suspend fun persistData(apiData: U) {
            dataPersister(apiData)
        }
    }
    return resource.flow()
}