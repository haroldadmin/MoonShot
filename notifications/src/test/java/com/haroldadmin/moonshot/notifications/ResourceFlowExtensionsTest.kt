package com.haroldadmin.moonshot.notifications

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.notifications.new.segregate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class ResourceFlowExtensionsTest {

    @Test
    fun `should segregate successfully when data fetch is successful`() = runBlocking {
        val flow = flow<Resource<Int>> {
            emit(Resource.Uninitialized)
            emit(Resource.Loading)
            emit(Resource.Success(1, isCached = true))
            emit(Resource.Success(2, isCached = false))
        }
        val segregatedRes = flow.segregate()

        assert(segregatedRes.cached == 1)
        assert(segregatedRes.new == 2)
    }

    @Test
    fun `should segregate successfully when data fetch is unsuccessful`() = runBlocking {
        val flow = flow<Resource<Int>> {
            emit(Resource.Uninitialized)
            emit(Resource.Loading)
            emit(Resource.Success(1, isCached = true))
            emit(Resource.Error(null, null))
        }
        val segregatesRes = flow.segregate()

        assert(segregatesRes.cached == 1)
        assert(segregatesRes.new == null)
    }

    @Test
    fun `should segregate successfully when flow is invalid`() = runBlocking {
        val flow = flow<Resource<Int>> {
            emit(Resource.Uninitialized)
            emit(Resource.Uninitialized)
        }
        val segregatedResource = flow.segregate()

        assert(segregatedResource.cached == null)
        assert(segregatedResource.new == null)
    }

    @Test
    fun `should segregate successfully when there is no cached data`() = runBlocking {
        val flow = flow<Resource<Unit>> {
            emit(Resource.Loading)
            emit(Resource.Error(null, null))
        }
        val segregatedResource = flow.segregate()

        assert(segregatedResource.cached == null)
        assert(segregatedResource.new == null)
    }

}