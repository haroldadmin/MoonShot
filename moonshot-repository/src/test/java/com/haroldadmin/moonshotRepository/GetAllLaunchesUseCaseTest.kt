package com.haroldadmin.moonshotRepository

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.launch.toLaunchMinimal
import com.haroldadmin.moonshotRepository.launch.GetAllLaunchesUseCase
import com.haroldadmin.moonshotRepository.mappers.toDbLaunch
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.reduce
import java.io.IOException

@ExperimentalCoroutinesApi
class GetAllLaunchesUseCaseTest : DescribeSpec({

    describe("No cached launches") {

        context("Network request successful") {

            val fakeApiLaunches = FakeDataProvider.getApiLaunches(10)
            val fakeDbLaunches = FakeDataProvider.getDbLaunches(0)

            val mockDao = mockk<LaunchDao> {
                coEvery {
                    getAllLaunchesMinimal(any(), any(), any())
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
                val flow = GetAllLaunchesUseCase(mockDao, mockService).getAllLaunches()
                val resource = flow.first()

                resource.shouldBeTypeOf<Resource.Loading>()
            }

            it("Should not emit empty resource") {
                val flow = GetAllLaunchesUseCase(mockDao, mockService).getAllLaunches()

                flow
                    .drop(1) // Drop Resource.Loading
                    .onStart {
                        coEvery {
                            mockDao.getAllLaunchesMinimal(any(), any(), any())
                        } returns FakeDataProvider.getDbLaunches(10)
                    }
                    .collect { resource ->
                        resource.shouldBeTypeOf<Resource.Success<List<LaunchMinimal>>>()
                    }
            }
        }

        context("Network request unsuccessful") {
            val fakeDbLaunches = FakeDataProvider.getDbLaunches(0)

            val mockDao = mockk<LaunchDao> {
                coEvery { getAllLaunchesMinimal(any(), any(), any()) } returns fakeDbLaunches
            }

            val mockService = mockk<LaunchesService> {
                every {
                    getAllLaunches()
                } returns CompletableDeferred(NetworkResponse.NetworkError(IOException()))
            }

            it("Should emit Resource.Loading first") {
                val flow = GetAllLaunchesUseCase(mockDao, mockService).getAllLaunches()
                val resource = flow.first()

                resource.shouldBeTypeOf<Resource.Loading>()
            }

            it("Should result in Resource.Error") {
                val flow = GetAllLaunchesUseCase(mockDao, mockService).getAllLaunches()
                flow
                    .drop(1) // Drop Resource.Loading
                    .collect { resource ->
                        resource.shouldBeTypeOf<Resource.Error<List<LaunchMinimal>, IOException>>()
                    }
            }
        }
    }

    describe("Cached Launches present") {

        context("Network request unsuccessful") {

            val fakeDbLaunches = FakeDataProvider.getDbLaunches(10)

            val mockDao = mockk<LaunchDao> {
                coEvery {
                    getAllLaunchesMinimal(any(), any(), any())
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
                val flow = GetAllLaunchesUseCase(mockDao, mockService).getAllLaunches()
                val resource = flow.first()

                resource.shouldBeTypeOf<Resource.Loading>()
            }

            it("Should emit cached launches after loading") {
                val flow = GetAllLaunchesUseCase(mockDao, mockService).getAllLaunches()

                val resource = flow
                    .drop(1) // Drop Resource.Loading
                    .first()

                resource.shouldBeTypeOf<Resource.Success<List<LaunchMinimal>>>()
                resource as Resource.Success

                resource.data shouldBe fakeDbLaunches
            }

            @Suppress("UNCHECKED_CAST")
            it("Should emit Resource.Error with cached data in the end") {
                val flow = GetAllLaunchesUseCase(mockDao, mockService).getAllLaunches()

                val lastEmittedResource = flow.reduce { _, value -> value }

                lastEmittedResource.shouldBeTypeOf<Resource.Error<List<LaunchMinimal>, IOException>>()
                lastEmittedResource as Resource.Error<List<LaunchMinimal>, IOException>
                lastEmittedResource.data shouldBe fakeDbLaunches
            }
        }

        context("Network request successful") {
            val fakeApiLaunches = FakeDataProvider.getApiLaunches(10)
            val fakeDbLaunches = FakeDataProvider.getDbLaunches(10)

            val mockDao = mockk<LaunchDao> {
                coEvery {
                    getAllLaunchesMinimal(any(), any(), any())
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
                val flow = GetAllLaunchesUseCase(mockDao, mockService).getAllLaunches()
                val resource = flow.first()

                resource.shouldBeTypeOf<Resource.Loading>()
            }

            it("Should emit cached launches first") {
                val flow = GetAllLaunchesUseCase(mockDao, mockService).getAllLaunches()
                val resource = flow
                    .drop(1) // Drop Resource.Loading
                    .first()

                resource.shouldBeTypeOf<Resource.Success<List<LaunchMinimal>>>()
                resource as Resource.Success
                resource.data shouldBe fakeDbLaunches
            }

            it("Should eventually emit newly fetched launches") {
                val flow = GetAllLaunchesUseCase(mockDao, mockService).getAllLaunches()
                flow
                    .drop(2) // Drop Loading and Cached responses
                    .onStart {
                        coEvery {
                            mockDao.getAllLaunchesMinimal(any(), any(), any())
                        } returns fakeApiLaunches.map { it.toDbLaunch().toLaunchMinimal() }
                    }
                    .collect { resource ->
                        resource.shouldBeTypeOf<Resource.Success<List<LaunchMinimal>>>()
                        resource as Resource.Success
                        resource.data shouldBe fakeApiLaunches.map { it.toDbLaunch().toLaunchMinimal() }
                    }
            }
        }
    }

})