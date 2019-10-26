package com.haroldadmin.moonshotRepository.search

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.models.SearchQuery
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshotRepository.launchPad.FakeLaunchPadDao
import com.haroldadmin.moonshotRepository.launchPad.FakeLaunchPadService
import com.haroldadmin.moonshotRepository.launchPad.PersistLaunchPadUseCase
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
internal class SearchLaunchPadsUseCaseTest : AnnotationSpec() {

    private val dao = FakeLaunchPadDao()
    private val service = FakeLaunchPadService()
    private val persister = PersistLaunchPadUseCase(dao)
    private val usecase = SearchLaunchpadsUseCase(dao, service, persister)

    @Test
    fun `should search for launches with given query`() = runBlocking {
        val query = SearchQuery("Falcon 9")

        val emittedRes = usecase.searchFor(query, limit = 10).last()

        with(emittedRes) {
            shouldBeTypeOf<Resource.Success<List<Launch>>>()
            this as Resource.Success

            data.all { it.siteNameLong.contains(query.query) } shouldBe true
        }
    }

}