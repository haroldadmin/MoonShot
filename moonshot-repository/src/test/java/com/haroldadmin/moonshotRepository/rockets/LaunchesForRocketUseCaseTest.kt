package com.haroldadmin.moonshotRepository.rockets

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshotRepository.launch.PersistLaunchesUseCase
import com.haroldadmin.moonshotRepository.launches.FakeLaunchesDao
import com.haroldadmin.moonshotRepository.launches.FakeLaunchesService
import com.haroldadmin.moonshotRepository.rocket.GetLaunchesForRocketUseCase
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
internal class LaunchesForRocketUseCaseTest : AnnotationSpec() {

    private val dao = FakeRocketsDao()
    private val launchesDao = FakeLaunchesDao()
    private val service = FakeLaunchesService()
    private val persister = PersistLaunchesUseCase(launchesDao)
    private val usecase = GetLaunchesForRocketUseCase(dao, persister, service)

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
}