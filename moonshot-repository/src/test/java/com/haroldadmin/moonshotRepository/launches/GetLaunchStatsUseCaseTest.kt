package com.haroldadmin.moonshotRepository.launches

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.database.launch.LaunchDao
import com.haroldadmin.moonshot.models.launch.LaunchStats
import com.haroldadmin.moonshotRepository.launch.GetLaunchStatsUseCase
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.specs.DescribeSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class GetLaunchStatsUseCaseTest : DescribeSpec({
    describe("Fetching launch stats") {
        val testID = 1
        val mockDao = mockk<LaunchDao> {
            coEvery { getLaunchStats(any()) } returns mockk()
        }

        val usecase = GetLaunchStatsUseCase(mockDao)

        it("Should fetch stats for requested flight ID") {
            val res = usecase.getLaunchStats(testID).last()

            with(res) {
                shouldBeTypeOf<Resource.Success<LaunchStats>>()
            }

            coVerify {
                mockDao.getLaunchStats(testID)
            }
        }
    }
})