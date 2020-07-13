package com.haroldadmin.moonshot.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.runBlocking

internal class LandingPadTest: AnnotationSpec() {

    @Test
    fun testLandingPadModel() {
        val landingPadJson = useJSON("/sampleData/v4/one_landingpad.json")
        val landingPadAdapter = useJSONAdapter<LandingPad>()

        val landingPad = landingPadAdapter.fromJson(landingPadJson)

        with(landingPad) {
            this.shouldNotBeNull()
            name shouldBe "LZ-2"
            status shouldBe "active"
            launchIDs shouldHaveSize 3
        }
    }

    @Test
    fun testOneLandingPadResponse() {
        val (service, cleanup) = useMockService<LandingPadService> {
            setBody(useJSON("/sampleData/v4/one_landingpad.json"))
        }

        val id = "5e9e3032383ecb90a834e7c8"
        val response = runBlocking { service.one(id) }
        response.shouldBeInstanceOf<NetworkResponse.Success<LandingPad>>()
        response as NetworkResponse.Success

        response.body.id shouldBe id

        cleanup()
    }

    @Test
    fun testAllLandingPadsResponse() {
        val (service, cleanup) = useMockService<LandingPadService> {
            setBody(useJSON("/sampleData/v4/all_landingpads.json"))
        }

        val response = runBlocking { service.all() }
        response.shouldBeInstanceOf<NetworkResponse.Success<List<LandingPad>>>()
        response as NetworkResponse.Success

        response.body shouldHaveSize  1

        cleanup()
    }

}