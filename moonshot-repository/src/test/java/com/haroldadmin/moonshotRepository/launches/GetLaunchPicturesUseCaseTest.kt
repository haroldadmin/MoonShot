package com.haroldadmin.moonshotRepository.launches

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.LaunchPictures
import com.haroldadmin.moonshotRepository.launch.GetLaunchPicturesUseCase
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.specs.DescribeSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class GetLaunchPicturesUseCaseTest : DescribeSpec({
    describe("Fetching pictures for requested launch") {
        val testID = 1

        val mockDao = mockk<LaunchDao> {
            coEvery { getLaunchPictures(any()) } returns LaunchPictures(listOf())
        }

        val useCase = GetLaunchPicturesUseCase(mockDao)

        it("Should fetch pictures of requested launch") {
            val res = useCase.getLaunchPictures(testID).last()
            with(res) {
                shouldBeTypeOf<Resource.Success<LaunchPictures>>()
            }

            coVerify { mockDao.getLaunchPictures(testID) }
        }
    }
})