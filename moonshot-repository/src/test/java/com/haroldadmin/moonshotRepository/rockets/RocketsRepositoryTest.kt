package com.haroldadmin.moonshotRepository.rockets

import com.haroldadmin.moonshot.database.rocket.RocketsDao
import com.haroldadmin.moonshotRepository.rocket.RocketsRepository
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import io.kotlintest.specs.DescribeSpec
import io.mockk.mockk
import io.mockk.spyk

@Suppress("DeferredResultUnused")
class RocketsRepositoryTest : DescribeSpec() {

    private val dao: RocketsDao = spyk(FakeRocketsDao())
    private val service = mockk<RocketsService>()
    private val repository = RocketsRepository(service, dao)
    /**
    init {

    describe("Rockets Repository") {
    context("Get all rockets successfully") {

    every { service.getAllRockets() } returns CompletableDeferred(
    NetworkResponse.Success(listOf(), null)
    )

    val rockets = repository.getAllRockets()

    it("Should call the api service") {
    verify { service.getAllRockets() }
    }

    it("Should call the dao") {
    coVerify {
    dao.getAllRockets()
    dao.saveRocketsWithPayloadWeights(any(), any())
    }
    }

    it("Should return Resource.Success") {
    rockets.shouldBeTypeOf<Resource.Success<List<Rocket>>>()
    }
    }

    context("Get all rockets with server error") {

    every { service.getAllRockets() } returns CompletableDeferred(
    NetworkResponse.ServerError(null, 404)
    )

    val rockets = repository.getAllRockets()

    it("Should call the api service to get all rockets") {
    verify { service.getAllRockets() }
    }

    it("Should call the dao only to get the cached values") {
    coVerify { dao.getAllRockets() }
    coVerify {
    dao.save(any()) wasNot called
    dao.saveAll(any<List<DbRocket>>()) wasNot called
    dao.savePayloadWeights(any()) wasNot called
    dao.saveRocketWithPayloadWeights(any(), any()) wasNot called
    }
    }

    it("Should return Resource.Error") {
    rockets.shouldBeTypeOf<Resource.Error<List<Rocket>, ErrorResponse>>()
    rockets as Resource.Error<List<DbRocket>, ErrorResponse>
    rockets.data shouldBe listOf()
    rockets.error shouldBe null
    }
    }

    context("Get all rockets with Network Error") {

    every { service.getAllRockets() } returns CompletableDeferred(
    NetworkResponse.NetworkError(IOException())
    )

    val rockets = repository.getAllRockets()

    it("Should call the api to get all rockets") {
    verify { service.getAllRockets() }
    }

    it("Should call the dao only to get the cached values") {
    coVerify { dao.getAllRockets() }
    coVerify {
    dao.save(any()) wasNot called
    dao.saveAll(any<List<DbRocket>>()) wasNot called
    dao.savePayloadWeights(any()) wasNot called
    dao.saveRocketWithPayloadWeights(any(), any()) wasNot called
    }
    }

    it("Should return Resource.Error") {
    rockets.shouldBeTypeOf<Resource.Error<List<Rocket>, IOException>>()
    rockets as Resource.Error<List<DbRocket>, IOException>
    rockets.data shouldBe listOf()
    rockets.error shouldNotBe null
    }
    }

    context("Get one rocket successfully") {
    val rocketId = "falcon9"
    val mockedRocket = mockk<Rocket>()
    mockkStatic("com.haroldadmin.moonshotRepository.mappers.RocketKt")
    every { mockedRocket.rocketId } returns rocketId
    every { mockedRocket.toDbRocket() } returns DbRocket.getSampleRocket()
    every { mockedRocket.payloadWeights } returns listOf()
    every { service.getRocket(rocketId) } returns CompletableDeferred(
    NetworkResponse.Success(mockedRocket)
    )

    val rocket = repository.getRocket(rocketId)

    it("Should call the api service to get the rocket") {
    verify { service.getRocket(rocketId) }
    }

    it("Should call the dao to save and retrieve the data") {
    coVerify {
    dao.save(any())
    dao.getRocket(rocketId)
    }
    }

    it("Should return successfully") {
    rocket.shouldBeTypeOf<Resource.Success<Rocket>>()
    rocket as Resource.Success
    rocket.data.rocketId shouldBe rocketId
    }
    }

    context("Get one rocket with server error") {
    val rocketId = "falcon9"

    every { service.getRocket(rocketId) } returns CompletableDeferred(
    NetworkResponse.ServerError(null, 404)
    )

    val rocket = repository.getRocket(rocketId)

    it("Should call the api service") {
    verify { service.getRocket(rocketId) }
    }

    it("Should call the dao only to fetch cached value") {
    coVerify {
    dao.getRocket(rocketId)
    dao.save(any()) wasNot called
    dao.saveRocketWithPayloadWeights(any(), any()) wasNot called
    }
    }

    it("Should return Resource.Error") {
    rocket.shouldBeTypeOf<Resource.Error<Rocket, ErrorResponse>>()
    rocket as Resource.Error<DbRocket, ErrorResponse>
    rocket.error shouldBe null
    }
    }

    context("Get one rocket with network error") {

    val rocketId = "falcon9"
    every { service.getRocket(rocketId) } returns CompletableDeferred(
    NetworkResponse.NetworkError(IOException())
    )

    val rocket = repository.getRocket(rocketId)

    it("Should call the API service") {
    verify { service.getRocket(rocketId) }
    }

    it("Should call dao only to retrieve the cached value") {
    coVerify {
    dao.getRocket(rocketId)
    dao.save(any()) wasNot called
    dao.saveRocketWithPayloadWeights(any(), any()) wasNot called
    }
    }

    it("Should result in Resource.Error") {
    rocket.shouldBeTypeOf<Resource.Error<Rocket, IOException>>()
    }
    }
    }
    }
     **/
}