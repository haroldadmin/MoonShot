package com.haroldadmin.moonshotRepository.launches

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshotRepository.ExpectedResponse
import com.haroldadmin.moonshotRepository.launch.GetLaunchDetailsUseCase
import com.haroldadmin.moonshotRepository.launch.PersistLaunchesUseCase
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue

@ExperimentalCoroutinesApi
internal class LaunchDetailsUseCaseTest : AnnotationSpec() {

    private lateinit var dao: FakeLaunchesDao
    private lateinit var service: FakeLaunchesService
    private lateinit var persister: PersistLaunchesUseCase
    private lateinit var usecase: GetLaunchDetailsUseCase

    @Before
    fun setup() {
        dao = FakeLaunchesDao()
        service = FakeLaunchesService()
        persister = PersistLaunchesUseCase(dao)
        usecase = GetLaunchDetailsUseCase(dao, service, persister)
    }

    @Test
    fun shouldEmitCachedDataAfterLoading() = runBlocking {
        val flightNumber = 1
        val flow = usecase.getLaunchDetails(flightNumber)

        val emittedResource = flow.first()

        with(emittedResource) {

            shouldBeTypeOf<Resource.Success<Launch>>()
            this as Resource.Success

            isCached shouldBe true
            data.flightNumber shouldBe flightNumber
        }
    }

    @Test
    fun shouldEmitRefreshedDataWhenNetworkSuccessful() = runBlocking {
        val flightNumber = 1
        service.expectedResponse = ExpectedResponse.Success

        val flow = usecase.getLaunchDetails(flightNumber)
        val emittedResource = flow.last()

        with(emittedResource) {

            shouldBeTypeOf<Resource.Success<Launch>>()
            this as Resource.Success

            isCached shouldBe false
        }
    }

    @Test
    fun shouldEmitCachedDataWhenNetworkError() = runBlocking {
        val flightNumber = 1
        service.expectedResponse = ExpectedResponse.NetworkError

        val flow = usecase.getLaunchDetails(flightNumber)
        val emittedResource = flow.last()

        with(emittedResource) {

            shouldBeTypeOf<Resource.Error<Launch, ErrorResponse>>()
            @Suppress("UNCHECKED_CAST")
            this as Resource.Error<Launch, ErrorResponse>

            data shouldNotBe null
            data!!.flightNumber shouldBe flightNumber
        }
    }

    @Test
    fun `should only fetch data from the API once`() = runBlocking {
        service.expectedResponse = ExpectedResponse.Success
        repeat(5) {
            usecase.getLaunchDetails(10).last()
        }
        assertTrue(service.requestCount == 1)
    }
}
