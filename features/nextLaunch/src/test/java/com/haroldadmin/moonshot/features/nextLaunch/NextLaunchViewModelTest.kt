package com.haroldadmin.moonshot.features.nextLaunch

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.features.nextLaunch.NextLaunchState
import com.haroldadmin.moonshot.features.nextLaunch.NextLaunchViewModel
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class NextLaunchViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("Main thread")
    private val initState =
        NextLaunchState()
    private val nextLaunchUseCase = mockk<GetNextLaunchUseCase> {
        coEvery { getNextLaunch() } returns flowOf(Resource.Loading)
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
    fun givenNextLaunchVM_onInit_thenShouldFetchLaunchDetails() = runBlockingTest {
        val viewModel =
            NextLaunchViewModel(
                initState,
                nextLaunchUseCase
            )
        viewModel.getNextLaunch()
        coVerify { nextLaunchUseCase.getNextLaunch() }
    }
}