package com.haroldadmin.moonshotRepository.launches

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.models.launch.LaunchPictures
import com.haroldadmin.moonshotRepository.launch.GetLaunchPicturesUseCase
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
internal class LaunchPicturesUseCaseTest: AnnotationSpec() {

    private val dao = FakeLaunchesDao()
    private val usecase = GetLaunchPicturesUseCase(dao)

    @Test
    fun `should return pictures for the requested launch`() = runBlocking {
        val flightNumber = 1
        val flow = usecase.getLaunchPictures(flightNumber)

        val emittedResource = flow.last()
        val savedPictures = dao.pictures(flightNumber)

        with(emittedResource) {
            shouldBeTypeOf<Resource.Success<LaunchPictures>>()
            this as Resource.Success<LaunchPictures>

            data shouldBe savedPictures
        }
    }
}