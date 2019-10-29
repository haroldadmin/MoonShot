package com.haroldadmin.moonshotRepository

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.core.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

/**
 * Represents a resource which needs to be loaded from the network and persisted to the Database
 *
 * @param T The type of the entity to be fetched/stored in the database
 * @param U The success type of [NetworkResponse]
 * @param V The error type of [NetworkResponse]
 *
 * The items emitted from this class are wrapped in a [Resource] class. The items are emitted in a [Flow].
 * Following is sequence of actions taken:
 * 1. Emit Resource.Loading
 * 2. Query database for cached data using [getFromDatabase].
 * 3. Emit Cached data from the database, if there is any. The decision to emit data is made using the abstract
 * [validateCache] method.
 * 4. Fetch data from the API using [getFromApi]
 * 5. If the fetch is successful, persist the data using [persistData] else emit [Resource.Error] with the cached data and terminate
 * 6. Emit [Resource.Success] with the newly persisted data if the fetch was successful
 *
 * The class also contains two properties [offset] and [limit] so that they can be dynamically updated. They are
 * passed to [getFromDatabase] on every invocation. They can be updated using the [updateParams] method.
 */
abstract class NetworkBoundResource<T : Any, U : Any, V : Any> {

    protected var offset: Int = 0
    protected var limit: Int = -1

    abstract suspend fun getFromDatabase(isRefreshed: Boolean, limit: Int, offset: Int): T?
    abstract suspend fun validateCache(cachedData: T?): Boolean
    abstract suspend fun getFromApi(): NetworkResponse<U, V>
    abstract suspend fun persistData(apiData: U)

    fun updateParams(limit: Int = this.limit, offset: Int = this.offset) {
        this.offset = offset
        this.limit = limit
    }

    @ExperimentalCoroutinesApi
    open fun flow(): Flow<Resource<T>> {
        return flow {
            val cachedData = getFromDatabase(isRefreshed = false, limit = limit, offset = offset)
            if (validateCache(cachedData)) {
                emit(Resource.Success(cachedData!!, isCached = true))
            }

            when (val apiResponse = getFromApi()) {
                is NetworkResponse.Success -> {
                    persistData(apiResponse.body)
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
        }.onStart { emit(Resource.Loading) }
    }
}

@ExperimentalCoroutinesApi
inline fun <T : Any, U : Any, V : Any> networkBoundResource(
    crossinline initialParams: () -> Pair<Int, Int> = { -1 to 0 },
    crossinline dbFetcher: suspend (Boolean, Int, Int) -> T?,
    crossinline apiFetcher: suspend () -> NetworkResponse<U, V>,
    crossinline cacheValidator: suspend (T?) -> Boolean,
    crossinline dataPersister: suspend (U) -> Unit
): NetworkBoundResource<T, U, V> {
    return object : NetworkBoundResource<T, U, V>() {
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
inline fun <T : Any, U : Any, V : Any> networkBoundFlow(
    crossinline initialParams: () -> Pair<Int, Int> = { -1 to 0 },
    crossinline dbFetcher: suspend (Boolean, Int, Int) -> T?,
    crossinline apiFetcher: suspend () -> NetworkResponse<U, V>,
    crossinline cacheValidator: suspend (T?) -> Boolean,
    crossinline dataPersister: suspend (U) -> Unit
): Flow<Resource<T>> {
    val resource = networkBoundResource(initialParams, dbFetcher, apiFetcher, cacheValidator, dataPersister)
    return resource.flow()
}