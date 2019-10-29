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
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue

@ExperimentalCoroutinesApi
internal class LaunchesUseCaseTest : AnnotationSpec() {

    private lateinit var dao: FakeLaunchesDao
    private lateinit var service: FakeLaunchesService
    private lateinit var persister: PersistLaunchesUseCase
    private lateinit var usecase: GetLaunchesUseCase

    @Before
    fun setup() {
        dao = FakeLaunchesDao()
        service = FakeLaunchesService()
        persister = PersistLaunchesUseCase(dao)
        usecase = GetLaunchesUseCase(dao, service, persister)
    }

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

    @Test
    fun `should replace all non existent launches in API result from DB when synchronizing`() = runBlocking {
        service.expectedResponse = ExpectedResponse.Success
        usecase.sync()
        assertTrue(dao.didSynchronize)
    }

    @Test
    fun `should only fetch data from the API once`() = runBlocking {
        service.expectedResponse = ExpectedResponse.Success
        repeat(5) {
            usecase.getLaunches(LaunchType.All, limit = 10).last()
        }
        assertTrue(service.requestCount == 1)
    }

    @Test
    fun `paging test`() = runBlocking {
        service.expectedResponse = ExpectedResponse.Success
        val limit = 10
        var offset = 0
        usecase
            .getLaunches(LaunchType.All, limit, offset)
            .filterIsInstance<Resource.Success<List<Launch>>>()
            .flatMapLatest {
                offset += limit
                usecase.getLaunches(LaunchType.All, limit, offset)
            }
            .last()

        dao.lastCallOffset shouldBe offset
    }
}