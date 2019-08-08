package com.haroldadmin.moonshotRepository

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshotRepository.launch.GetLaunchesForLaunchpadUseCase
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.reduce
import java.io.IOException
import java.util.Date

@ExperimentalCoroutinesApi
class GetLaunchesForLaunchpadUseCaseTest : DescribeSpec({

    describe("Selection of launches based on given range of time") {

        val usecase = spyk(GetLaunchesForLaunchpadUseCase(mockk(), mockk()))

        context("Get all launches") {
            val from = Long.MIN_VALUE
            val to = Long.MAX_VALUE
            val current = Date().time
            val limit = 10

            usecase.getLaunchesForLaunchpad("test-id", from, to, current, limit)

            it("Should get all launches") {
                coVerify(exactly = 1) {
                    usecase.getAllLaunches("test-id", limit)
                }
            }
        }

        context("Get past launches") {
            val currentTime = Date().time
            val from = Long.MIN_VALUE
            val to = currentTime
            val current = currentTime
            val limit = 10

            usecase.getLaunchesForLaunchpad("test-id", from, to, current, limit)

            it("Should get past launches") {
                coVerify(exactly = 1) {
                    usecase.getPastLaunches("test-id", current, limit)
                }
            }
        }

        context("Get upcoming launches") {
            val currentTime = Date().time
            val from = currentTime + 1
            val to = Long.MAX_VALUE
            val current = currentTime
            val limit = 10

            usecase.getLaunchesForLaunchpad("test-id", from, to, current, limit)

            it("Should get upcoming launches") {
                coVerify(exactly = 1) {
                    usecase.getUpcomingLaunches("test-id", current, limit)
                }
            }
        }
    }

    describe("Emitted events when there are no cached launches") {
        val fakeApiLaunches = FakeDataProvider.getApiLaunches(10)
        val mockDao = mockk<LaunchDao> {
            coEvery {
                getAllLaunchesForLaunchPad(any(), any())
            } returns listOf()

            coEvery {
                saveLaunchesWithSummaries(any(), any(), any(), any(), any(), any())
            } returns Unit
        }

        context("Network request is successful") {

            val mockApi = mockk<LaunchesService> {
                every {
                    getAllLaunches(siteId = any())
                } returns CompletableDeferred(NetworkResponse.Success(fakeApiLaunches))
            }

            it("Should emit Resource.Loading first") {
                val flow = GetLaunchesForLaunchpadUseCase(mockDao, mockApi)
                    .getLaunchesForLaunchpad("test-id", Long.MIN_VALUE, Long.MAX_VALUE, Date().time)

                val firstEmittedResource = flow.first()

                firstEmittedResource.shouldBeTypeOf<Resource.Loading>()
            }

            it("Should not emit cached launches") {
                val flow = GetLaunchesForLaunchpadUseCase(mockDao, mockApi)
                    .getLaunchesForLaunchpad("test-id", Long.MIN_VALUE, Long.MAX_VALUE, Date().time)
                    .drop(1) // Drop Resource.Loading

                val resource = flow.first()

                resource.shouldBeTypeOf<Resource.Success<List<LaunchMinimal>>>()
                (resource as Resource.Success).isCached shouldBe false
            }
        }

        context("Network Request is unsuccessful") {

            val mockApi = mockk<LaunchesService> {
                every {
                    getAllLaunches(siteId = any())
                } returns CompletableDeferred(NetworkResponse.NetworkError(IOException()))
            }

            it("Should emit Resource.Loading first") {
                val flow = GetLaunchesForLaunchpadUseCase(mockDao, mockApi)
                    .getLaunchesForLaunchpad("test-id", Long.MIN_VALUE, Long.MAX_VALUE, Date().time)

                val firstEmittedResource = flow.first()

                firstEmittedResource.shouldBeTypeOf<Resource.Loading>()
            }

            it("Should emit Resource.Error eventually") {
                val resource = GetLaunchesForLaunchpadUseCase(mockDao, mockApi)
                    .getLaunchesForLaunchpad("test-id", Long.MIN_VALUE, Long.MAX_VALUE, Date().time)
                    .reduce { _, value -> value } // Get Last emitted resource

                resource.shouldBeTypeOf<Resource.Error<List<LaunchMinimal>, IOException>>()

                @Suppress("UNCHECKED_CAST")
                resource as Resource.Error<List<LaunchMinimal>, IOException>

                resource.data.shouldNotBeNull()
                resource.data!!.shouldHaveSize(0)
            }
        }
    }

    describe("Emitted events when there are cached launches") {
        val fakeApiLaunches = FakeDataProvider.getApiLaunches(10)

        val mockDao = mockk<LaunchDao> {
            coEvery {
                getAllLaunchesForLaunchPad(any(), any())
            } returns FakeDataProvider.getDbLaunches(10)

            coEvery {
                saveLaunchesWithSummaries(any(), any(), any(), any(), any(), any())
            } returns Unit
        }

        context("Network request is successful") {
            val mockApi = mockk<LaunchesService> {
                every {
                    getAllLaunches(siteId = any())
                } returns CompletableDeferred(NetworkResponse.Success(fakeApiLaunches))
            }

            it("Should emit Resource.Loading first") {
                val flow = GetLaunchesForLaunchpadUseCase(mockDao, mockApi)
                    .getLaunchesForLaunchpad("test-id", Long.MIN_VALUE, Long.MAX_VALUE, Date().time)

                val firstEmittedResource = flow.first()

                firstEmittedResource.shouldBeTypeOf<Resource.Loading>()
            }

            it("Should emit cached launches") {
                val flow = GetLaunchesForLaunchpadUseCase(mockDao, mockApi)
                    .getLaunchesForLaunchpad("test-id", Long.MIN_VALUE, Long.MAX_VALUE, Date().time)
                    .drop(1) // Drop Resource.Loading

                val resource = flow.first()

                resource.shouldBeTypeOf<Resource.Success<List<LaunchMinimal>>>()
                (resource as Resource.Success).isCached shouldBe true
            }

            it("Should eventually emit refreshed launches") {
                val lastEmittedResource = GetLaunchesForLaunchpadUseCase(mockDao, mockApi)
                    .getLaunchesForLaunchpad("test-id", Long.MIN_VALUE, Long.MAX_VALUE, Date().time)
                    .reduce { _, value -> value } // Get last emitted value

                lastEmittedResource.shouldBeTypeOf<Resource.Success<List<LaunchMinimal>>>()
                (lastEmittedResource as Resource.Success).isCached shouldBe false
            }
        }
    }
})