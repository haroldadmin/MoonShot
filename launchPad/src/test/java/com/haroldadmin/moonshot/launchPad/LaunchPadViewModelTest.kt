package com.haroldadmin.moonshot.launchPad

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshotRepository.launchPad.GetLaunchPadUseCase
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

internal class LaunchPadViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("Main thread")
    private val initState = LaunchPadState("test-id")
    private val useCase = mockk<GetLaunchPadUseCase> {
        coEvery { getLaunchPad(any()) } returns flowOf(Resource.Loading)
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
    fun `given LaunchPadVM, when fetching launchpad, then should call usecase`() = runBlockingTest {
        val viewModel = LaunchPadViewModel(initState, useCase)
        viewModel.getLaunchPad("test-id")
        coVerify { useCase.getLaunchPad("test-id") }
    }
}