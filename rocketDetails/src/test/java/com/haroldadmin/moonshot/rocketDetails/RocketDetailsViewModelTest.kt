package com.haroldadmin.moonshot.rocketDetails

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.rocket.RocketMinimal
import com.haroldadmin.moonshotRepository.rocket.GetLaunchesForRocketUseCase
import com.haroldadmin.moonshotRepository.rocket.GetRocketDetailsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class RocketDetailsViewModelTest {

    val mainThreadSurrogate = newSingleThreadContext("Main Thread")
    val initialState = RocketDetailsState("test-id")
    val rocket = mockk<Resource.Success<RocketMinimal>>()
    val launches = mockk<Resource.Success<List<LaunchMinimal>>>()
    val rocketDetailsUseCase = mockk<GetRocketDetailsUseCase> {
        coEvery { getRocketDetails(any()) } returns flowOf(rocket)
    }
    val launchesForRocketUseCase = mockk<GetLaunchesForRocketUseCase> {
        coEvery { getLaunchesForRocket(any(), any(), any()) } returns flowOf(launches)
    }

    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun givenRocketDetailsViewModel_whenInit_shouldFetchRocketDetails() {
        RocketDetailsViewModel(initialState, rocketDetailsUseCase, launchesForRocketUseCase)
        coVerify {
            rocketDetailsUseCase.getRocketDetails("test-id")
            launchesForRocketUseCase.getLaunchesForRocket("test-id", any(), any())
        }
    }

}