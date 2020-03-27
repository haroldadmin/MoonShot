package com.haroldadmin.moonshotRepository.search

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.TestDispatchers
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.models.SearchQuery
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshotRepository.rocket.PersistRocketsUseCase
import com.haroldadmin.moonshotRepository.rockets.FakeRocketsDao
import com.haroldadmin.moonshotRepository.rockets.FakeRocketsService
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
internal class SearchRocketsUseCaseTest : AnnotationSpec() {

    private val dispatchers = TestDispatchers()

    private val dao = FakeRocketsDao()
    private val service = FakeRocketsService()
    private val persister = PersistRocketsUseCase(dao, dispatchers)
    private val usecase = SearchRocketsUseCase(dao, service, persister, dispatchers)

    @Test
    fun `should search for launches with given query`() = runBlocking {
        val query = SearchQuery("Falcon 9")

        val emittedRes = usecase.searchFor(query, limit = 10).last()

        with(emittedRes) {
            shouldBeTypeOf<Resource.Success<List<Launch>>>()
            this as Resource.Success

            data.all { it.rocketName.contains(query.query) } shouldBe true
        }
    }
}