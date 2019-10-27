package com.haroldadmin.moonshotRepository.launches

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import com.haroldadmin.moonshotRepository.launch.PersistLaunchesUseCase
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import java.util.Date

@ExperimentalCoroutinesApi
internal class NextLaunchUseCaseTest: AnnotationSpec() {

    private val dao = FakeLaunchesDao()
    private val service = FakeLaunchesService()
    private val persister = PersistLaunchesUseCase(dao)
    private val usecase = GetNextLaunchUseCase(dao, service, persister)

    @Test
    fun `should return nearest upcoming launch`() = runBlocking{
        val flow = usecase.getNextLaunch()
        val emittedResource = flow.last()
        with(emittedResource) {
            shouldBeTypeOf<Resource.Success<Launch>>()
            this as Resource.Success

            data.isUpcoming shouldBe true
        }
    }

    @Test
    fun `should return next launches until given date only`() = runBlocking {
        val now = Date()
        val weekFromNow = now.time + (86400 * 1000 * 7)

        val flow = usecase.getNextLaunchesUntilDate(until = weekFromNow)
        val emittedRes = flow.last()

        with(emittedRes) {
            shouldBeTypeOf<Resource.Success<List<Launch>>>()
            this as Resource.Success

            data.all { it.launchDateUtc.time <= weekFromNow }
        }
    }
}