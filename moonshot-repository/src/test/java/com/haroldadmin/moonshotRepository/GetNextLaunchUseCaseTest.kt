package com.haroldadmin.moonshotRepository

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.launch.toLaunchMinimal
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.reduce

@ExperimentalCoroutinesApi
class GetNextLaunchUseCaseTest : DescribeSpec({

    describe("Behaviour when cache is valid") {
        val dbLaunch: LaunchMinimal = Launch.getSampleLaunch().toLaunchMinimal()
        val apiLaunch = FakeDataProvider.getApiLaunches(1).first()
        val mockDao = mockk<LaunchDao> {
            coEvery { getNextLaunch(any()) } returns dbLaunch
            coEvery {
                saveLaunchWithSummaries(any(), any(), any(), any(), any(), any())
            } returns Unit
        }

        val mockService = mockk<LaunchesService> {
            every {
                getNextLaunch()
            } returns CompletableDeferred(NetworkResponse.Success(apiLaunch))
        }

        val usecase = GetNextLaunchUseCase(mockDao, mockService)

        context("Network request is successful") {
            val resource = usecase.getNextLaunch(0L)

            it("Should emit cached data") {
                val res = resource
                    .drop(1) // Drop Resource.Loading
                    .first()

                with(res) {
                    shouldBeTypeOf<Resource.Success<LaunchMinimal>>()
                    this as Resource.Success
                    isCached shouldBe true
                    data shouldBe dbLaunch
                }
            }

            it("Should eventually emit refreshed data") {
                val res = resource.reduce { _, value -> value }

                with(res) {
                    shouldBeTypeOf<Resource.Success<LaunchMinimal>>()
                    this as Resource.Success
                    isCached shouldBe false
                }
            }
        }
    }

})