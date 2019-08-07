package com.haroldadmin.moonshotRepository

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.database.launchPad.LaunchPadDao
import com.haroldadmin.moonshot.models.launchpad.LaunchPad
import com.haroldadmin.moonshotRepository.launchPad.GetLaunchPadUseCase
import com.haroldadmin.moonshotRepository.mappers.toDbLaunchPad
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPadService
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPad as ApiLaunchPad
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class GetLaunchPadUseCaseTest: DescribeSpec({

    describe("Fetching launchpad with given Site ID") {
        val testSiteId = "Test ID"
        val testDbLaunchPad = LaunchPad.getSampleLanchpad(testSiteId)
        val testApiLaunchPad = mockk<ApiLaunchPad>()

        val mockDao = mockk<LaunchPadDao> {
            coEvery { getLaunchPad(testSiteId) } returns testDbLaunchPad
            coEvery { save(any()) } returns Unit

        }
        val mockService = mockk<LaunchPadService> {
            every {
                getLaunchPad(testSiteId)
            } returns CompletableDeferred(NetworkResponse.Success(testApiLaunchPad))
        }

        mockkStatic("com.haroldadmin.moonshotRepository.mappers.LaunchPadKt")
        every { testApiLaunchPad.toDbLaunchPad() } returns testDbLaunchPad

        val useCase = GetLaunchPadUseCase(mockDao, mockService)

        it("Should return launchpad with requested Site ID") {
            val resource = useCase.getLaunchPad(testSiteId).last()
            with(resource) {
                shouldBeTypeOf<Resource.Success<LaunchPad>>()
                this as Resource.Success
                data shouldBe testDbLaunchPad
                data.siteId shouldBe testSiteId
            }
        }
    }

})