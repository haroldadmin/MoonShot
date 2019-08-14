package com.haroldadmin.moonshotRepository.search

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshotRepository.SingleFetchNetworkBoundResource
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * A [searchResource] is a [SingleFetchNetworkBoundResource] which
 * needs to update its query and results limit dynamically, without recreating
 * the entire resource.
 */
@ExperimentalCoroutinesApi
inline fun <T : Any, U : Any, V : Any> searchResource(
    crossinline dbQuery: suspend () -> String,
    crossinline dbLimit: suspend () -> Int,
    crossinline dbFetcher: suspend (Boolean, String, Int) -> T?,
    crossinline apiFetcher: suspend () -> NetworkResponse<U, V>,
    crossinline cacheValidator: suspend (T?) -> Boolean,
    crossinline dataPersister: suspend (U) -> Unit
): SingleFetchNetworkBoundResource<T, U, V> {
    return object : SingleFetchNetworkBoundResource<T, U, V>() {
        override suspend fun getFromDatabase(isRefreshed: Boolean): T? {
            return dbFetcher(isRefreshed, dbQuery(), dbLimit())
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
