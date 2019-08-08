package com.haroldadmin.moonshotRepository.launches

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshotRepository.FakeDataProvider
import com.haroldadmin.moonshotRepository.launch.GetLaunchDetailsUseCase
import com.haroldadmin.moonshotRepository.launch.PersistLaunchesUseCase
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.specs.DescribeSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred

class GetLaunchDetailsUseCaseTest: DescribeSpec({
    describe("Fetching launch details") {
        val testID = 1
        val dbLaunch = FakeDataProvider.getDbLaunch()
        val apiLaunch = FakeDataProvider.getApiLaunch()

        val mockDao = mockk<LaunchDao> {
            coEvery { getLaunch(any()) } returns dbLaunch
        }

        val mockService = mockk<LaunchesService> {
            every {
                getLaunch(any())
            } returns CompletableDeferred(NetworkResponse.Success(apiLaunch))
        }

        val mockPersistUseCase = mockk<PersistLaunchesUseCase> {
            coEvery { persistLaunch(any()) } returns Unit
        }

        val useCase = GetLaunchDetailsUseCase(mockDao, mockService, mockPersistUseCase)

        it("Should fetch launch details of request flight ID") {
            val res = useCase.getLaunchDetails(testID).last()
            with(res) {
                shouldBeTypeOf<Resource.Success<LaunchMinimal>>()
            }

            coVerify {
                mockDao.getLaunch(testID)
            }

            verify {
                mockService.getLaunch(testID)
            }
        }
    }
})