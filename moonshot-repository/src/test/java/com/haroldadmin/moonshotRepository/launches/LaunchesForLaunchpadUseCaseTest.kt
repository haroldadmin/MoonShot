package com.haroldadmin.moonshotRepository.launches

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshotRepository.ExpectedResponse
import com.haroldadmin.moonshotRepository.launch.GetLaunchesForLaunchpadUseCase
import com.haroldadmin.moonshotRepository.launch.LaunchType
import com.haroldadmin.moonshotRepository.launch.PersistLaunchesUseCase
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
internal class LaunchesForLaunchpadUseCaseTest : AnnotationSpec() {

    private lateinit var dao: FakeLaunchesDao
    private lateinit var service: FakeLaunchesService
    private lateinit var persister: PersistLaunchesUseCase
    private lateinit var usecase: GetLaunchesForLaunchpadUseCase

    @Before
    fun setup() {
        dao = FakeLaunchesDao()
        service = FakeLaunchesService()
        persister = PersistLaunchesUseCase(dao)
        usecase = GetLaunchesForLaunchpadUseCase(dao, service, persister)
    }

    @Test
    fun `should emit cached data after loading`() = runBlocking {
        val siteId = "ccafs_slc_40"
        val flow = usecase.getLaunchesForLaunchpad(siteId, LaunchType.All)

        val emittedResource = flow.first()

        with(emittedResource) {

            shouldBeTypeOf<Resource.Success<List<Launch>>>()
            this as Resource.Success

            isCached shouldBe true
            data.all { it.launchSite!!.siteId == siteId } shouldBe true
        }
    }

    @Test
    fun `should emit refreshed data when network req is successful`() = runBlocking {
        val siteId = "ccafs_slc_40"
        service.expectedResponse = ExpectedResponse.Success

        val flow = usecase.getLaunchesForLaunchpad(siteId, LaunchType.All)
        val emittedResource = flow.last()

        with(emittedResource) {

            shouldBeTypeOf<Resource.Success<List<Launch>>>()
            this as Resource.Success

            isCached shouldBe false
        }
    }

    @Test
    fun `should emit cached data when network req is unsuccessful`() = runBlocking {
        val siteId = "ccafs_slc_40"
        service.expectedResponse = ExpectedResponse.NetworkError

        val flow = usecase.getLaunchesForLaunchpad(siteId, LaunchType.All)
        val emittedResource = flow.last()

        with(emittedResource) {

            shouldBeTypeOf<Resource.Error<List<Launch>, ErrorResponse>>()
            @Suppress("UNCHECKED_CAST")
            this as Resource.Error<List<Launch>, ErrorResponse>

            data shouldNotBe null
            data!!.all { it.launchSite!!.siteId == siteId } shouldBe true
        }
    }

    @Test
    fun `should return upcoming launches when asked for upcoming launches`() = runBlocking {
        val siteId = "ccafs_slc_40"
        service.expectedResponse = ExpectedResponse.Success

        val flow = usecase.getLaunchesForLaunchpad(siteId, LaunchType.Upcoming)
        val emittedResource = flow.last()

        with(emittedResource) {
            shouldBeTypeOf<Resource.Success<List<Launch>>>()
            this as Resource.Success

            data.all { it.isUpcoming == true } shouldBe true
        }
    }

    @Test
    fun `should return past launches when asked for past launches`() = runBlocking {
        val siteId = "ccafs_slc_40"
        service.expectedResponse = ExpectedResponse.Success

        val flow = usecase.getLaunchesForLaunchpad(siteId, LaunchType.Recent)
        val emittedResource = flow.last()

        with(emittedResource) {
            shouldBeTypeOf<Resource.Success<List<Launch>>>()
            this as Resource.Success

            data.all { it.isUpcoming == false } shouldBe true
        }
    }

    @Test
    fun `paging test`() = runBlocking {
        service.expectedResponse = ExpectedResponse.Success
        val siteId = "ccafs_slc_40"
        val limit = 10
        var offset = 0
        usecase
            .getLaunchesForLaunchpad(siteId, LaunchType.All, limit, offset)
            .filterIsInstance<Resource.Success<List<Launch>>>()
            .flatMapLatest {
                offset += limit
                usecase.getLaunchesForLaunchpad(siteId, LaunchType.All, limit, offset)
            }
            .last()

        dao.lastCallOffset shouldBe offset
    }
}