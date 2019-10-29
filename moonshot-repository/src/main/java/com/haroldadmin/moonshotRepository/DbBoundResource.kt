package com.haroldadmin.moonshotRepository

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.pairOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

/**
 * Represents a resource which needs to be fetched from the database only
 *
 * @param T The type of the entity to be fetched/stored in the database
 *
 * The items emitted from this class are wrapped in a [Resource] class. The items are emitted in a [Flow].
 * Following is sequence of actions taken:
 * 1. Emit Resource.Loading
 * 2. Query database for cached data using [fetchFromDb], and emit [Resource.Success] if it passes [validateData]
 * else emit [Resource.Error]
 *
 * The class also contains two properties [offset] and [limit] so that they can be dynamically updated. They are
 * passed to [getFromDatabase] on every invocation. They can be updated using the [updateParams] method.
 */
abstract class DbBoundResource<T : Any> {

    private var dbLimit: Int = -1
    private var dbOffset: Int = 0

    abstract suspend fun fetchFromDb(): T?
    abstract suspend fun validateData(data: T?): Boolean

    @ExperimentalCoroutinesApi
    fun flow(): Flow<Resource<T>> = flow {
        val cached = fetchFromDb()
        if (validateData(cached)) {
            emit(Resource.Success(cached!!))
        } else {
            emit(Resource.Error(data = null, error = null))
        }
    }.onStart { emit(Resource.Loading) }

    fun updateParams(limit: Int = dbLimit, offset: Int = dbOffset) {
        dbLimit = limit
        dbOffset = offset
    }
}

@ExperimentalCoroutinesApi
inline fun <T : Any> dbBoundResource(
    crossinline initialParams: () -> Pair<Int, Int> = { pairOf(-1, 0) },
    crossinline dbFetcher: suspend () -> T?,
    crossinline validator: suspend (T?) -> Boolean
): Flow<Resource<T>> {
    val resource = object : DbBoundResource<T>() {
        init {
            val (limit, offset) = initialParams()
            updateParams(limit, offset)
        }
        override suspend fun fetchFromDb(): T? {
            return dbFetcher()
        }

        override suspend fun validateData(data: T?): Boolean {
            return validator(data)
        }
    }
    return resource.flow()
}