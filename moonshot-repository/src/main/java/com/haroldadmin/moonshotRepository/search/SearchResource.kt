package com.haroldadmin.moonshotRepository.search

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.models.SearchQuery
import com.haroldadmin.moonshotRepository.SingleFetchNetworkBoundResource

abstract class SearchResource<T : Any, U : Any, V : Any> : SingleFetchNetworkBoundResource<T, U, V>() {
    protected var query: String = ""

    fun updateParams(query: SearchQuery, dbLimit: Int = limit, dbOffset: Int = offset) {
        this.query = query.sqlQuery()
        super.updateParams(dbLimit, dbOffset)
    }
}

inline fun <T : Any, U : Any, V : Any> searchResourceLazy(
    crossinline dbFetcher: suspend (Boolean, String, Int, Int) -> T?,
    crossinline cacheValidator: suspend (T?) -> Boolean,
    crossinline apiFetcher: suspend () -> NetworkResponse<U, V>,
    crossinline dataPersister: suspend (U) -> Unit
): Lazy<SearchResource<T, U, V>> {
    return lazyOf(object : SearchResource<T, U, V>() {

        override suspend fun getFromDatabase(isRefreshed: Boolean, limit: Int, offset: Int): T? {
            return dbFetcher(isRefreshed, query, limit, offset)
        }

        override suspend fun validateCache(cachedData: T?): Boolean {
            return cacheValidator(cachedData)
        }

        override suspend fun getFromApi(): NetworkResponse<U, V> {
            return apiFetcher()
        }

        override suspend fun persistData(apiData: U) {
            return dataPersister(apiData)
        }
    })
}
