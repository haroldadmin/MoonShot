package com.haroldadmin.moonshotRepository.rockets

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.database.rocket.RocketsDao
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshotRepository.FakeDataProvider
import com.haroldadmin.moonshotRepository.launch.PersistLaunchesUseCase
import com.haroldadmin.moonshotRepository.rocket.GetLaunchesForRocketUseCase
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.coVerify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class GetLaunchesForRocketUseCastTest : DescribeSpec({

    describe("Fetching launches for a rocket") {
        val testID = "Test ID"
        val dbLaunches = FakeDataProvider.getDbLaunches(2)
        val apiLaunches = FakeDataProvider.getApiLaunches(2)

        val mockDao = mockk<RocketsDao> {
            coEvery { getLaunchesForRocket(any(), any(), any()) } returns dbLaunches
        }

        val mockService = mockk<LaunchesService> {
            every {
                getAllLaunches()
            } returns CompletableDeferred(NetworkResponse.Success(apiLaunches))
        }

        val mockPersistUseCase = mockk<PersistLaunchesUseCase> {
            coEvery { persistLaunches(any()) } returns Unit
        }

        it("Should fetch launches for the Rocket with given ID") {
            val usecase = GetLaunchesForRocketUseCase(mockDao, mockPersistUseCase, mockService)
            val res = usecase.getLaunchesForRocket(testID, 0L).last()

            with(res) {
                shouldBeTypeOf<Resource.Success<List<LaunchMinimal>>>()
                this as Resource.Success
                data shouldBe dbLaunches
            }

            coVerify { mockDao.getLaunchesForRocket(testID, any(), any()) }
        }
    }
})