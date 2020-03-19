package com.haroldadmin.moonshot.notifications

import com.haroldadmin.moonshot.core.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * A data class to represent cached and newly refreshed parts of a
 * Resource Flow.
 */
internal data class SegregatedResource<T>(
    val cached: T?,
    val new: T?
)

/**
 * Collects the given resource flow and returns a [SegregatedResource]
 * with properly separated cached and newly refreshed values.
 */
internal suspend fun <T> Flow<Resource<T>>.segregate(): SegregatedResource<T> {
    var cached: T? = null
    var new: T? = null
    collect { res ->
        when (res) {
            is Resource.Success -> {
                if (res.isCached) cached = res.data
                else if (!res.isCached) new = res.data
                else Unit
            }
            is Resource.Error<T, *> -> {
                new = res.data
            }
            Resource.Loading -> Unit
            Resource.Uninitialized -> Unit
        }
    }
    return SegregatedResource(cached, new)
}