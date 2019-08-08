package com.haroldadmin.moonshotRepository

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.launch.toLaunchMinimal
import com.haroldadmin.moonshotRepository.launch.GetLaunchesUseCase
import com.haroldadmin.moonshotRepository.launch.LaunchesFilter
import com.haroldadmin.moonshotRepository.mappers.toDbLaunch
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.reduce
import java.io.IOException
import java.util.Date

@ExperimentalCoroutinesApi
class GetLaunchesUseCaseTest : DescribeSpec({

    describe("Selection of launches based on given range of time") {

        val usecase = spyk(GetLaunchesUseCase(mockk(), mockk()))

        context("Get all launches") {
            val current = Date().time
            val limit = 10

            usecase.getLaunches(LaunchesFilter.ALL, current, limit)

            it("Should get all launches") {
                coVerify(exactly = 1) {
                    usecase.getAllLaunches(limit)
                }
            }
        }

        context("Get past launches") {
            val currentTime = Date().time
            val limit = 10

            usecase.getLaunches(LaunchesFilter.PAST, currentTime, limit)

            it("Should get past launches") {
                coVerify(exactly = 1) {
                    usecase.getPastLaunches(currentTime, limit)
                }
            }
        }

        context("Get upcoming launches") {
            val currentTime = Date().time
            val limit = 10

            usecase.getLaunches(LaunchesFilter.UPCOMING, currentTime, limit)

            it("Should get upcoming launches") {
                coVerify(exactly = 1) {
                    usecase.getUpcomingLaunches(currentTime, limit)
                }
            }
        }
    }

    describe("Emitted events when there are no cached launches") {

        context("Network request successful") {

            val fakeApiLaunches = FakeDataProvider.getApiLaunches(10)
            val fakeDbLaunches = FakeDataProvider.getDbLaunches(0)

            val mockDao = mockk<LaunchDao> {
                coEvery {
                    getAllLaunches(any())
                } returns fakeDbLaunches

                coEvery {
                    saveLaunchesWithSummaries(any(), any(), any(), any(), any(), any())
                } returns Unit
            }

            val mockService = mockk<LaunchesService> {
                every {
                    getAllLaunches()
                } returns CompletableDeferred(NetworkResponse.Success(fakeApiLaunches))
            }

            it("Should emit Resource.Loading first") {
                val flow = GetLaunchesUseCase(mockDao, mockService)
                    .getLaunches(LaunchesFilter.ALL, Date().time)
                val resource = flow.first()

                resource.shouldBeTypeOf<Resource.Loading>()
            }

            it("Should emit newly fetched launches in the end") {
                val flow = GetLaunchesUseCase(mockDao, mockService)
                    .getLaunches(LaunchesFilter.ALL, Date().time)

                coEvery {
                    mockDao.getAllLaunches(any())
                } returns fakeApiLaunches.map { it.toDbLaunch().toLaunchMinimal() }

                flow
                    .drop(2) // Drop Resource.Loading and cached response
                    .collect { resource ->
                        resource.shouldBeTypeOf<Resource.Success<List<LaunchMinimal>>>()
                        resource as Resource.Success
                        resource.isCached shouldBe false
                    }
            }
        }

        context("Network request unsuccessful") {
            val fakeDbLaunches = FakeDataProvider.getDbLaunches(0)

            val mockDao = mockk<LaunchDao> {
                coEvery { getAllLaunches(any()) } returns fakeDbLaunches
            }

            val mockService = mockk<LaunchesService> {
                every {
                    getAllLaunches()
                } returns CompletableDeferred(NetworkResponse.NetworkError(IOException()))
            }

            it("Should emit Resource.Loading first") {
                val flow = GetLaunchesUseCase(mockDao, mockService)
                    .getLaunches(LaunchesFilter.ALL, Date().time)
                val resource = flow.first()

                resource.shouldBeTypeOf<Resource.Loading>()
            }

            it("Should result in Resource.Error") {
                val flow = GetLaunchesUseCase(mockDao, mockService)
                    .getLaunches(LaunchesFilter.ALL, Date().time)
                flow
                    .drop(1) // Drop Resource.Loading
                    .collect { resource ->
                        resource.shouldBeTypeOf<Resource.Error<List<LaunchMinimal>, IOException>>()
                    }
            }
        }
    }

    describe("Emitted events when cached launches are present") {

        context("Network request unsuccessful") {

            val fakeDbLaunches = FakeDataProvider.getDbLaunches(10)

            val mockDao = mockk<LaunchDao> {
                coEvery {
                    getAllLaunches(any())
                } returns fakeDbLaunches

                coEvery {
                    saveLaunchesWithSummaries(any(), any(), any(), any(), any(), any())
                } returns Unit
            }

            val mockService = mockk<LaunchesService> {
                every {
                    getAllLaunches()
                } returns CompletableDeferred(NetworkResponse.NetworkError(IOException()))
            }

            it("Should emit Resource.Loading first") {
                val flow = GetLaunchesUseCase(mockDao, mockService)
                    .getLaunches(LaunchesFilter.ALL, Date().time)
                val resource = flow.first()

                resource.shouldBeTypeOf<Resource.Loading>()
            }

            it("Should emit cached launches after loading") {
                val flow = GetLaunchesUseCase(mockDao, mockService)
                    .getLaunches(LaunchesFilter.ALL, Date().time)

                val resource = flow
                    .drop(1) // Drop Resource.Loading
                    .first()

                with(resource) {
                    shouldBeTypeOf<Resource.Success<List<LaunchMinimal>>>()
                    this as Resource.Success
                    data shouldBe fakeDbLaunches
                    isCached shouldBe true
                }
            }

            @Suppress("UNCHECKED_CAST")
            it("Should emit Resource.Error with cached data in the end") {
                val flow = GetLaunchesUseCase(mockDao, mockService)
                    .getLaunches(LaunchesFilter.ALL, Date().time)

                val lastEmittedResource = flow.reduce { _, value -> value }

                with(lastEmittedResource) {
                    shouldBeTypeOf<Resource.Error<List<LaunchMinimal>, IOException>>()
                    this as Resource.Error<List<LaunchMinimal>, IOException>
                    data shouldBe fakeDbLaunches
                }
            }
        }

        context("Network request successful") {
            val fakeApiLaunches = FakeDataProvider.getApiLaunches(10)
            val fakeDbLaunches = FakeDataProvider.getDbLaunches(10)

            val mockDao = mockk<LaunchDao> {
                coEvery {
                    getAllLaunches(any())
                } returns fakeDbLaunches

                coEvery {
                    saveLaunchesWithSummaries(any(), any(), any(), any(), any(), any())
                } returns Unit
            }

            val mockService = mockk<LaunchesService> {
                every {
                    getAllLaunches()
                } returns CompletableDeferred(NetworkResponse.Success(fakeApiLaunches))
            }

            it("Should emit Resource.Loading first") {
                val flow = GetLaunchesUseCase(mockDao, mockService)
                    .getLaunches(LaunchesFilter.ALL, Date().time)
                val resource = flow.first()

                resource.shouldBeTypeOf<Resource.Loading>()
            }

            it("Should emit cached launches first") {
                val flow = GetLaunchesUseCase(mockDao, mockService)
                    .getLaunches(LaunchesFilter.ALL, Date().time)
                val resource = flow
                    .drop(1) // Drop Resource.Loading
                    .first()

                with(resource) {
                    shouldBeTypeOf<Resource.Success<List<LaunchMinimal>>>()
                    this as Resource.Success
                    data shouldBe fakeDbLaunches
                    isCached shouldBe true
                }
            }

            it("Should eventually emit newly fetched launches") {
                val flow = GetLaunchesUseCase(mockDao, mockService)
                    .getLaunches(LaunchesFilter.ALL, Date().time)

                flow
                    .drop(2) // Drop Loading and Cached responses
                    .onStart {
                        coEvery {
                            mockDao.getAllLaunches(any())
                        } returns fakeApiLaunches.map { it.toDbLaunch().toLaunchMinimal() }
                    }
                    .collect { resource ->
                        with(resource) {
                            shouldBeTypeOf<Resource.Success<List<LaunchMinimal>>>()
                            this as Resource.Success
                            data shouldBe fakeApiLaunches.map {
                                it.toDbLaunch().toLaunchMinimal()
                            }
                            isCached shouldBe false
                        }
                    }
            }
        }
    }
})