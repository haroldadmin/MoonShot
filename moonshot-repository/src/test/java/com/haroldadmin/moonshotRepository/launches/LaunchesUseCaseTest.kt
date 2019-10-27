package com.haroldadmin.moonshotRepository.launches

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshotRepository.ExpectedResponse
import com.haroldadmin.moonshotRepository.launch.GetLaunchesUseCase
import com.haroldadmin.moonshotRepository.launch.LaunchType
import com.haroldadmin.moonshotRepository.launch.PersistLaunchesUseCase
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
internal class LaunchesUseCaseTest : AnnotationSpec() {

    private val dao = FakeLaunchesDao()
    private val service = FakeLaunchesService()
    private val persister = PersistLaunchesUseCase(dao)
    private val usecase = GetLaunchesUseCase(dao, service, persister)

    @Test
    fun `should emit Loading first`() = runBlocking {
        val flow = usecase.getLaunches(LaunchType.All)
        flow.first() shouldBe Resource.Loading
    }

    @Test
    fun `should emit cached data after loading`() = runBlocking {
        val flow = usecase.getLaunches(LaunchType.All)

        val emittedResource = flow.drop(1).first()

        with(emittedResource) {

            shouldBeTypeOf<Resource.Success<List<Launch>>>()
            this as Resource.Success

            isCached shouldBe true
        }
    }

    @Test
    fun `should emit refreshed data when network req is successful`() = runBlocking {
        service.expectedResponse = ExpectedResponse.Success

        val flow = usecase.getLaunches(LaunchType.All)
        val emittedResource = flow.last()

        with(emittedResource) {

            shouldBeTypeOf<Resource.Success<List<Launch>>>()
            this as Resource.Success

            isCached shouldBe false
        }
    }

    @Test
    fun `should emit cached data when network req is unsuccessful`() = runBlocking {
        service.expectedResponse = ExpectedResponse.NetworkError

        val flow = usecase.getLaunches(LaunchType.All)
        val emittedResource = flow.last()

        with(emittedResource) {

            shouldBeTypeOf<Resource.Error<List<Launch>, ErrorResponse>>()
            @Suppress("UNCHECKED_CAST")
            this as Resource.Error<List<Launch>, ErrorResponse>

            data shouldNotBe null
        }
    }

    @Test
    fun `should return upcoming launches when asked for upcoming launches`() = runBlocking {
        service.expectedResponse = ExpectedResponse.Success

        val flow = usecase.getLaunches(LaunchType.Upcoming)
        val emittedResource = flow.last()

        with(emittedResource) {
            shouldBeTypeOf<Resource.Success<List<Launch>>>()
            this as Resource.Success

            data.all { it.isUpcoming == true } shouldBe true
        }
    }

    @Test
    fun `should return past launches when asked for past launches`() = runBlocking {
        service.expectedResponse = ExpectedResponse.Success

        val flow = usecase.getLaunches(LaunchType.Recent)
        val emittedResource = flow.last()

        with(emittedResource) {
            shouldBeTypeOf<Resource.Success<List<Launch>>>()
            this as Resource.Success

            data.all { it.isUpcoming == false } shouldBe true
        }
    }
}