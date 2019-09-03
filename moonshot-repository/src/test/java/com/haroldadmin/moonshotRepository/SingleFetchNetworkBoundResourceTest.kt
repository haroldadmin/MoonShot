package com.haroldadmin.moonshotRepository

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import java.io.IOException

private class Api {
    var fetchCount = 0
    var fetchErrorCount = 0

    fun get(): NetworkResponse.Success<Unit> {
        fetchCount++
        return NetworkResponse.Success(Unit)
    }

    fun getErrorThenSuccess(): NetworkResponse<Unit, IOException> {
        return if (fetchErrorCount == 0) {
            fetchErrorCount++
            fetchCount++
            NetworkResponse.NetworkError(IOException())
        } else {
            fetchCount++
            NetworkResponse.Success(Unit)
        }
    }
}

@ExperimentalCoroutinesApi
internal class SingleFetchNetworkBoundResourceTest : DescribeSpec({

    describe("Single fetch behaviour") {

        context("Data has not been fetched yet") {
            val api = Api()
            val resource = singleFetchNetworkBoundFlow(
                dbFetcher = { Unit },
                cacheValidator = { cached -> cached != null },
                apiFetcher = { api.get() },
                dataPersister = { Unit }
            )

            it("Should fetch data from API when invoked") {
                resource.collect()
                api.fetchCount shouldBe 1
            }

            it("Should not fetch data from API again when invoked again") {
                resource.collect()
                api.fetchCount shouldBe 1
            }
        }

        context("Last network request resulted in error") {
            val api = Api()
            val resource = singleFetchNetworkBoundFlow(
                dbFetcher = { Unit },
                cacheValidator = { cached -> cached != null },
                apiFetcher = { api.getErrorThenSuccess() },
                dataPersister = { Unit }
            )

            it("Should fetch data from API when invoked") {
                val res = resource.last()
                api.fetchCount shouldBe 1
                res.shouldBeTypeOf<Resource.Error<Unit, IOException>>()
            }

            it("Should fetch data again from API when invoked") {
                val res = resource.last()
                api.fetchCount shouldBe 2
                res.shouldBeTypeOf<Resource.Success<Unit>>()
            }
        }
    }
})