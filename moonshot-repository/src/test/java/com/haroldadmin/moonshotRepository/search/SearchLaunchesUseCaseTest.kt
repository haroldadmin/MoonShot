package com.haroldadmin.moonshotRepository.search

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.TestDispatchers
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.models.SearchQuery
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshotRepository.launch.PersistLaunchesUseCase
import com.haroldadmin.moonshotRepository.launches.FakeLaunchesDao
import com.haroldadmin.moonshotRepository.launches.FakeLaunchesService
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
internal class SearchLaunchesUseCaseTest : AnnotationSpec() {

    private val dispatchers = TestDispatchers()

    private val dao = FakeLaunchesDao()
    private val service = FakeLaunchesService()
    private val persister = PersistLaunchesUseCase(dao, dispatchers)
    private val usecase = SearchLaunchesUseCase(dao, service, persister, dispatchers)

    @Test
    fun `should search for launches with given query`() = runBlocking {
        val query = SearchQuery("Starlink")

        val emittedRes = usecase.searchFor(query, limit = 10).last()

        with(emittedRes) {
            shouldBeTypeOf<Resource.Success<List<Launch>>>()
            this as Resource.Success

            data.all { it.missionName.contains(query.query) } shouldBe true
        }
    }
}