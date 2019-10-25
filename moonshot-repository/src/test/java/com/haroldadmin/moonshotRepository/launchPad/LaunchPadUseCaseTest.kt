package com.haroldadmin.moonshotRepository.launchPad

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPad
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
internal class LaunchPadUseCaseTest : AnnotationSpec() {

    private val dao = FakeLaunchPadDao()
    private val service = FakeLaunchPadService()
    private val persister = PersistLaunchPadUseCase(dao)
    private val usecase = GetLaunchPadUseCase(dao, service, persister)

    @Test
    fun `should fetch launchpad with given ID`() = runBlocking {
        val siteId = "ccafs_slc_40"
        val flow = usecase.getLaunchPad(siteId)

        val emittedResource = flow.last()

        with(emittedResource) {
            shouldBeTypeOf<Resource.Success<LaunchPad>>()
            this as Resource.Success

            data.siteId shouldBe siteId
        }
    }
}