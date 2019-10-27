package com.haroldadmin.moonshotRepository.rockets

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.models.Rocket
import com.haroldadmin.moonshotRepository.ExpectedResponse
import com.haroldadmin.moonshotRepository.rocket.GetAllRocketsUseCase
import com.haroldadmin.moonshotRepository.rocket.PersistRocketsUseCase
import com.haroldadmin.spacex_api_wrapper.common.ErrorResponse
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
internal class AllRocketsUseCaseTest : AnnotationSpec() {

    private val dao = FakeRocketsDao()
    private val service = FakeRocketsService()
    private val persister = PersistRocketsUseCase(dao)
    private val usecase = GetAllRocketsUseCase(dao, service, persister)

    @Test
    fun `should return rockets successfully when network is available`() = runBlocking {
        service.expectedType = ExpectedResponse.Success

        val flow = usecase.getAllRockets(limit = 10)

        val emittedRes = flow.last()

        with(emittedRes) {
            shouldBeTypeOf<Resource.Success<List<Rocket>>>()
            this as Resource.Success
            data shouldHaveSize 10
        }
    }

    @Test
    fun `should return cached rockets when network is unavailable`() = runBlocking {
        service.expectedType = ExpectedResponse.NetworkError

        val flow = usecase.getAllRockets(limit = 10)

        val emittedRes = flow.last()

        with(emittedRes) {
            shouldBeTypeOf<Resource.Error<List<Rocket>, ErrorResponse>>()
            @Suppress("UNCHECKED_CAST")
            this as Resource.Error<List<Rocket>, ErrorResponse>

            data shouldNotBe null
            data!! shouldHaveSize 10
        }
    }
}