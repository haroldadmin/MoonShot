package com.haroldadmin.moonshotRepository.launchPad

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshotRepository.ExpectedResponse
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPad
import io.kotlintest.matchers.collections.shouldHaveAtMostSize
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue

@ExperimentalCoroutinesApi
internal class LaunchPadUseCaseTest : AnnotationSpec() {

    private lateinit var dao: FakeLaunchPadDao
    private lateinit var service: FakeLaunchPadService
    private lateinit var usecase: GetLaunchPadUseCase
    private lateinit var persister: PersistLaunchPadUseCase

    @Before
    fun setup() {
        dao = FakeLaunchPadDao()
        service = FakeLaunchPadService()
        persister = PersistLaunchPadUseCase(dao)
        usecase = GetLaunchPadUseCase(dao, service, persister)
    }

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

    @Test
    fun `fetching all launchpads should return all launchpads`() = runBlocking {
        val count = 10
        val flow = usecase.getAllLaunchPads(limit = count)

        val emittedResource = flow.last()

        with(emittedResource) {
            shouldBeTypeOf<Resource.Success<List<LaunchPad>>>()
            this as Resource.Success

            data shouldHaveAtMostSize count
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
            usecase.getAllLaunchPads(limit = 10).last()
        }

        assertTrue(service.requestCount == 1)
    }
}