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

internal class RocketDetailsViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("Main Thread")
    private val initialState = RocketDetailsState("test-id")
    private val rocket = mockk<Resource.Success<RocketMinimal>>()
    private val launches = mockk<Resource.Success<List<LaunchMinimal>>>()
    private val rocketDetailsUseCase = mockk<GetRocketDetailsUseCase> {
        coEvery { getRocketDetails(any()) } returns flowOf(rocket)
    }
    private val launchesForRocketUseCase = mockk<GetLaunchesForRocketUseCase> {
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