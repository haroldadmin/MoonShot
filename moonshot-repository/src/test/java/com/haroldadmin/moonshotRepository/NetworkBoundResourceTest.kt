package com.haroldadmin.moonshotRepository

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.core.Resource
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.reduce
import java.io.IOException

@ExperimentalCoroutinesApi
class NetworkBoundResourceTest: DescribeSpec({

    describe("When cache is invalid") {
        val dbData = emptyList<Unit>()

        context("Successful network call") {
            val apiData = listOf(Unit)

            val resource = networkBoundFlow(
                dbFetcher = { isRefreshed -> if (isRefreshed) apiData else dbData },
                apiFetcher = { NetworkResponse.Success(apiData) },
                cacheValidator = { cachedData -> cachedData.isNotEmpty() },
                dataPersister = { Unit }
            )

            it("Should emit Resource.Loading first") {
                val firstEmittedRes = resource.first()
                firstEmittedRes.shouldBeTypeOf<Resource.Loading>()
            }

            it("Should not emit cached data") {
                resource
                    .drop(1) // Drop Resource.Loading
                    .collect { res ->
                        res.shouldBeTypeOf<Resource.Success<List<Unit>>>()
                        (res as Resource.Success).isCached shouldBe false
                }
            }

            it("Should emit API data only") {
                val lastEmittedRes = resource.reduce { _, value -> value }

                lastEmittedRes.shouldBeTypeOf<Resource.Success<List<Unit>>>()
                (lastEmittedRes as Resource.Success).data shouldBe apiData
            }
        }

        context("Unsuccessful network call") {

            val resource = networkBoundFlow(
                dbFetcher = { dbData },
                apiFetcher = { NetworkResponse.NetworkError(IOException()) },
                cacheValidator = { cachedData -> cachedData.isNotEmpty() },
                dataPersister = { Unit }
            )

            it("Should emit Resource.Loading first") {
                val firstEmittedRes = resource.first()
                firstEmittedRes.shouldBeTypeOf<Resource.Loading>()
            }

            it("Should result in Resource.Error") {
                val lastEmittedRes = resource.reduce { _, value -> value }

                lastEmittedRes.shouldBeTypeOf<Resource.Error<List<Unit>, *>>()
                (lastEmittedRes as Resource.Error<List<Unit>, *>).error.shouldBeTypeOf<IOException>()
            }
        }
    }

    describe("When cache is valid") {

        val dbData = listOf(Unit)

        context("Successful network call") {
           val apiData = listOf(Unit, Unit)

            val resource = networkBoundFlow(
                dbFetcher = { isRefreshed -> if (isRefreshed) apiData else dbData },
                apiFetcher = { NetworkResponse.Success(apiData) },
                cacheValidator = { cachedData -> cachedData.isNotEmpty() },
                dataPersister = { Unit }
            )

            it("Should emit Resource.Loading first") {
                val firstEmittedRes = resource.first()
                firstEmittedRes.shouldBeTypeOf<Resource.Loading>()
            }

            it("Should emit cached data") {
                val res = resource
                    .drop(1) // Drop Resource.Loading
                    .first()

                with(res) {
                    shouldBeTypeOf<Resource.Success<List<Unit>>>()
                    this as Resource.Success
                    isCached shouldBe true
                    data shouldBe dbData
                }
            }

            it("Should emit API data eventually") {
                val lastRes = resource.reduce { _, value -> value }
                with(lastRes) {
                    shouldBeTypeOf<Resource.Success<List<Unit>>>()
                    this as Resource.Success
                    isCached shouldBe false
                    data shouldBe apiData
                }
            }
        }

        context("Unsuccessful network call") {
            val resource = networkBoundFlow(
                dbFetcher = { dbData },
                apiFetcher = { NetworkResponse.NetworkError(IOException()) },
                cacheValidator = { cachedData -> cachedData.isNotEmpty() },
                dataPersister = { Unit }
            )

            it ("Should emit Resource.Loading first") {
                val firstRes = resource.first()
                firstRes.shouldBeTypeOf<Resource.Loading>()
            }

            it("Should emit cached data first") {
                val firstDataRes = resource.drop(1).first()

                with(firstDataRes) {
                    shouldBeTypeOf<Resource.Success<List<Unit>>>()
                    this as Resource.Success
                    isCached shouldBe true
                    data shouldBe dbData
                }
            }

            it("Should eventually emit Resource.Error with cached data") {
                val lastRes = resource.reduce { _, value -> value }

                with(lastRes) {
                    shouldBeTypeOf<Resource.Error<List<Unit>, *>>()
                    this as Resource.Error<List<Unit>, *>
                    data shouldBe dbData
                    error.shouldBeTypeOf<IOException>()
                }
            }
        }
    }
})