package com.haroldadmin.moonshot.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.services.spacex.v4.test.useJSON
import com.haroldadmin.moonshot.services.spacex.v4.test.useJSONAdapter
import com.haroldadmin.moonshot.services.spacex.v4.test.useMockService
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.runBlocking

internal class CrewTest: AnnotationSpec() {

    @Test
    fun testCrewModel() {
        val crewJson =
            useJSON("/sampleData/v4/one_crew.json")
        val crewAdapter =
            useJSONAdapter<Crew>()
        val crew = crewAdapter.fromJson(crewJson)

        with(crew) {
            this.shouldNotBeNull()
            name shouldBe "Douglas Hurley"
            launchIDs shouldHaveSize 1
        }
    }

    @Test
    fun testAllCrewResponse() {
        val (service, cleanup) = useMockService<CrewService> {
            setBody(com.haroldadmin.moonshot.services.spacex.v4.test.useJSON("/sampleData/v4/all_crew.json"))
        }

        val response = runBlocking { service.all() }
        response.shouldBeInstanceOf<NetworkResponse.Success<List<Crew>>>()
        response as NetworkResponse.Success

        response.body shouldHaveSize 2

        cleanup()
    }

    @Test
    fun testOneCrewResponse() {
        val (service, cleanup) = useMockService<CrewService> {
            setBody(com.haroldadmin.moonshot.services.spacex.v4.test.useJSON("/sampleData/v4/one_crew.json"))
        }

        val id = "5ebf1b7323a9a60006e03a7b"
        val response = runBlocking { service.one(id) }
        response.shouldBeInstanceOf<NetworkResponse.Success<Crew>>()
        response as NetworkResponse.Success

        response.body.id shouldBe id

        cleanup()
    }
}