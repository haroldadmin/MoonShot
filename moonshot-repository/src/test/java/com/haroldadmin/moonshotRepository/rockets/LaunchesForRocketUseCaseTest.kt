package com.haroldadmin.moonshotRepository.rockets

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshotRepository.ExpectedResponse
import com.haroldadmin.moonshotRepository.launch.PersistLaunchesUseCase
import com.haroldadmin.moonshotRepository.launches.FakeLaunchesDao
import com.haroldadmin.moonshotRepository.launches.FakeLaunchesService
import com.haroldadmin.moonshotRepository.rocket.GetLaunchesForRocketUseCase
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue

@ExperimentalCoroutinesApi
internal class LaunchesForRocketUseCaseTest : AnnotationSpec() {

    private lateinit var dao: FakeRocketsDao
    private lateinit var launchesDao: FakeLaunchesDao
    private lateinit var service: FakeLaunchesService
    private lateinit var persister: PersistLaunchesUseCase
    private lateinit var usecase: GetLaunchesForRocketUseCase

    @Before
    fun setup() {
        dao = FakeRocketsDao()
        launchesDao = FakeLaunchesDao()
        service = FakeLaunchesService()
        persister = PersistLaunchesUseCase(launchesDao)
        usecase = GetLaunchesForRocketUseCase(dao, persister, service)
    }

    @Test
    fun `should return launches for requested rocket`() = runBlocking {
        val rocketId = "falcon9"
        val flow = usecase.getLaunchesForRocket(rocketId, limit = 10)

        val emittedRes = flow.last()

        with(emittedRes) {
            shouldBeTypeOf<Resource.Success<List<Launch>>>()
            this as Resource.Success

            data.all { it.rocket.rocketId == rocketId } shouldBe true
        }
    }

    @Test
    fun `should only fetch data from the API once`() = runBlocking {
        service.expectedResponse = ExpectedResponse.Success
        repeat(5) {
            usecase.getLaunchesForRocket(rocketId = "falcon9", limit = 10).last()
        }

        assertTrue(service.requestCount == 1)
    }
}