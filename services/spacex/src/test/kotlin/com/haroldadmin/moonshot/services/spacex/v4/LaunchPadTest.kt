package com.haroldadmin.moonshot.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.runBlocking

internal class LaunchPadTest: AnnotationSpec() {

    @Test
    fun testLaunchPadModel() {
        val launchPadJson = useJSON("/sampleData/v4/one_launchpad.json")
        val launchPadAdapter = useJSONAdapter<LaunchPad>()

        val launchPad = launchPadAdapter.fromJson(launchPadJson)

        with(launchPad) {
            this.shouldNotBeNull()
            name shouldBe "VAFB SLC 4E"
            launchIDs shouldHaveSize 15
        }
    }

    @Test
    fun testOneLaunchPadResponse() {
        val (service, cleanup) = useMockService<LaunchPadsService> {
            setBody(useJSON("/sampleData/v4/one_launchpad.json"))
        }

        val id = "5e9e4502f509092b78566f87"
        val response = runBlocking { service.one(id) }
        response.shouldBeInstanceOf<NetworkResponse.Success<LaunchPad>>()
        response as NetworkResponse.Success

        response.body.id shouldBe id

        cleanup()
    }

    @Test
    fun testAllLaunchPadsResponse() {
        val (service, cleanup) = useMockService<LaunchPadsService> {
            setBody(useJSON("/sampleData/v4/all_launchpads.json"))
        }

        val response = runBlocking { service.all() }
        response.shouldBeInstanceOf<NetworkResponse.Success<List<LaunchPad>>>()
        response as NetworkResponse.Success

        response.body shouldHaveSize 1

        cleanup()
    }
}