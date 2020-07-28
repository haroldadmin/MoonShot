package com.haroldadmin.moonshot.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.services.spacex.v4.test.useJSON
import com.haroldadmin.moonshot.services.spacex.v4.test.useJSONAdapter
import com.haroldadmin.moonshot.services.spacex.v4.test.useMockService
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.runBlocking

internal class CoresTest: AnnotationSpec() {

    @Test
    fun testCoreModel() {
        val coreJson =
            useJSON("/sampleData/v4/one_core.json")
        val coreAdapter =
            useJSONAdapter<Core>()

        val core = coreAdapter.fromJson(coreJson)

        with(core) {
            this.shouldNotBeNull()
            id shouldBe "5e9e28a6f35918c0803b265c"
            launchIDs shouldHaveSize 4
        }
    }

    @Test
    fun testAllCoresResponse() {
        val (service, cleanup) = useMockService<CoreService> {
            setBody(com.haroldadmin.moonshot.services.spacex.v4.test.useJSON("/sampleData/v4/all_cores.json"))
        }

        val response = runBlocking { service.all() }
        response.shouldBeTypeOf<NetworkResponse.Success<List<Core>>>()
        response as NetworkResponse.Success

        response.body shouldHaveSize 66

        cleanup()
    }

    @Test
    fun testOneCoreResponse() {
        val (service, cleanup) = useMockService<CoreService> {
            setBody(com.haroldadmin.moonshot.services.spacex.v4.test.useJSON("/sampleData/v4/one_core.json"))
        }

        val id = "5e9e28a6f35918c0803b265c"
        val response = runBlocking { service.one(id) }
        response.shouldBeTypeOf<NetworkResponse.Success<Core>>()
        response as NetworkResponse.Success

        response.body.id shouldBe id

        cleanup()
    }
}