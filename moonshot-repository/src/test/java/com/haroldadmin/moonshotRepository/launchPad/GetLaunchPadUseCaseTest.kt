package com.haroldadmin.moonshotRepository.launchPad

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.database.launchPad.LaunchPadDao
import com.haroldadmin.moonshot.models.launchpad.LaunchPad
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPadService
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPad as ApiLaunchPad
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class GetLaunchPadUseCaseTest : DescribeSpec({

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

        val mockPersistUseCase = mockk<PersistLaunchPadUseCase> {
            coEvery { persistLaunchPad(any()) } returns Unit
        }

        val useCase = GetLaunchPadUseCase(mockDao, mockService, mockPersistUseCase)

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