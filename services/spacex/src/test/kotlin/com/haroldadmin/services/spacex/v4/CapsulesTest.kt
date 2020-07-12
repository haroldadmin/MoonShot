package com.haroldadmin.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.services.spacex.v4.adapters.ZonedDateTimeAdapter
import com.squareup.moshi.Moshi
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.runBlocking

internal class CapsulesTest: AnnotationSpec() {

    @Test
    fun testCapsuleModel() {
        val capsuleAdapter = useJSONAdapter<Capsule>()
        val capsuleJSON = useJSON("/sampleData/v4/one_capsule.json")

        val capsule = capsuleAdapter.fromJson(capsuleJSON)
        with(capsule) {
            this.shouldNotBeNull()
            reuseCount shouldBe 1
            waterLandings shouldBe 1
            landLandings shouldBe 0
            launchIDs shouldHaveSize 1
            serial shouldBe "C101"
            status shouldBe CapsuleStatus.retired
            id shouldBe "5e9e2c5bf35918ed873b2664"
        }
    }

    @Test
    fun testOneCapsuleResponse() {
        val (service, cleanup) = useMockService<CapsuleService> {
            setBody(useJSON("/sampleData/v4/one_capsule.json"))
        }

        val id = "5e9e2c5bf35918ed873b2664"
        val response = runBlocking { service.one("5e9e2c5bf35918ed873b2664") }

        response.shouldBeInstanceOf<NetworkResponse.Success<Capsule>>()
        response as NetworkResponse.Success

        response.body.id shouldBe id

        cleanup()
    }

    @Test
    fun testAllCapsulesResponse() {
        val (service, cleanup) = useMockService<CapsuleService> {
            setBody(useJSON("/sampleData/v4/all_capsules.json"))
        }

        val response = runBlocking { service.all() }

        response.shouldBeInstanceOf<NetworkResponse.Success<List<Capsule>>>()
        response as NetworkResponse.Success

        response.body shouldHaveSize 19

        cleanup()
    }
}