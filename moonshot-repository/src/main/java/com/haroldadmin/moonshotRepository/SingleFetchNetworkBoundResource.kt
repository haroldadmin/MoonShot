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
abstract class SingleFetchNetworkBoundResource<T : Any, U : Any, V : Any> : NetworkBoundResource<T, U, V>() {

    private var hasDataBeenFetched: Boolean = false

    @ExperimentalCoroutinesApi
    override fun flow(): Flow<Resource<T>> {
        return flow {
            val cachedData = getFromDatabase(isRefreshed = hasDataBeenFetched, limit = limit, offset = offset)

            if (validateCache(cachedData)) {
                emit(Resource.Success(cachedData!!, isCached = true))
            } else {
                emit(Resource.Loading)
            }

            if (!hasDataBeenFetched) {
                when (val apiResponse = getFromApi()) {
                    is NetworkResponse.Success -> {
                        persistData(apiResponse.body)
                        hasDataBeenFetched = true
                        val refreshedData = getFromDatabase(isRefreshed = true, limit = limit, offset = offset)!!
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
                cachedData?.let {
                    emit(Resource.Success(it, isCached = false))
                } ?: Resource.Error(cachedData, null)
            }
        }
    }
}

@ExperimentalCoroutinesApi
inline fun <T : Any, U : Any, V : Any> singleFetchNetworkBoundResource(
    crossinline initialParams: () -> Pair<Int, Int> = { -1 to 0 },
    crossinline dbFetcher: suspend (Boolean, Int, Int) -> T?,
    crossinline apiFetcher: suspend () -> NetworkResponse<U, V>,
    crossinline cacheValidator: suspend (T?) -> Boolean,
    crossinline dataPersister: suspend (U) -> Unit
): SingleFetchNetworkBoundResource<T, U, V> {
    return object : SingleFetchNetworkBoundResource<T, U, V>() {

        init {
            val (limit, offset) = initialParams()
            updateParams(limit, offset)
        }

        override suspend fun getFromDatabase(isRefreshed: Boolean, limit: Int, offset: Int): T? {
            return dbFetcher(isRefreshed, limit, offset)
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
}

@ExperimentalCoroutinesApi
inline fun <T : Any, U : Any, V : Any> singleFetchNetworkBoundResourceLazy(
    crossinline initialParams: () -> Pair<Int, Int> = { -1 to 0 },
    crossinline dbFetcher: suspend (Boolean, Int, Int) -> T?,
    crossinline apiFetcher: suspend () -> NetworkResponse<U, V>,
    crossinline cacheValidator: suspend (T?) -> Boolean,
    crossinline dataPersister: suspend (U) -> Unit
): Lazy<SingleFetchNetworkBoundResource<T, U, V>> {
    return lazyOf(singleFetchNetworkBoundResource(initialParams, dbFetcher, apiFetcher, cacheValidator, dataPersister))
}

@ExperimentalCoroutinesApi
inline fun <T : Any, U : Any, V : Any> singleFetchNetworkBoundFlow(
    crossinline initialParams: () -> Pair<Int, Int> = { -1 to 0 },
    crossinline dbFetcher: suspend (Boolean, Int, Int) -> T?,
    crossinline apiFetcher: suspend () -> NetworkResponse<U, V>,
    crossinline cacheValidator: suspend (T?) -> Boolean,
    crossinline dataPersister: suspend (U) -> Unit
): Flow<Resource<T>> {
    return singleFetchNetworkBoundResource(initialParams, dbFetcher, apiFetcher, cacheValidator, dataPersister).flow()
}