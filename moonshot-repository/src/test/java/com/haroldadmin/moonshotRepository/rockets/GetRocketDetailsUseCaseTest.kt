package com.haroldadmin.moonshotRepository.rockets

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.database.rocket.RocketsDao
import com.haroldadmin.moonshot.models.rocket.RocketMinimal
import com.haroldadmin.moonshotRepository.FakeDataProvider
import com.haroldadmin.moonshotRepository.rocket.GetRocketDetailsUseCase
import com.haroldadmin.moonshotRepository.rocket.PersistRocketsUseCase
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.specs.DescribeSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred

class GetRocketDetailsUseCaseTest: DescribeSpec({

    describe("Fetching rocket details with the given Rocket ID") {
        val testID = "Test ID"
        val mockDao =  mockk<RocketsDao> {
            coEvery { getRocket(any()) } returns FakeDataProvider.getDbRockets(
                1
            ).first()
            coEvery { saveRocketWithPayloadWeights(any(), any()) } returns Unit
        }
        val mockService = mockk<RocketsService> {
            every {
                getRocket(any())
            } returns CompletableDeferred(NetworkResponse.Success(FakeDataProvider.getApiRocket()))
        }

        val mockPersistUseCase = mockk<PersistRocketsUseCase> {
            coEvery { persistApiRocket(any()) } returns Unit
        }

        val useCase = GetRocketDetailsUseCase(mockDao, mockService, mockPersistUseCase)

        it("Should return Rocket with the requested Rocket ID") {
            val res = useCase.getRocketDetails(testID).last()
            with(res) {
                shouldBeTypeOf<Resource.Success<RocketMinimal>>()
                this as Resource.Success
            }

            coVerify {
                mockDao.getRocket(testID)
                mockService.getRocket(testID)
            }
        }
    }

})