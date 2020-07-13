package com.haroldadmin.moonshot.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.runBlocking
import java.time.Month

internal class RoadsterTest : AnnotationSpec() {

    @Test
    fun testRoadsterModel() {
        val roadsterJson = useJSON("/sampleData/v4/roadster_info.json")
        val roadsterAdapter = useJSONAdapter<RoadsterInfo>()

        val roadster = roadsterAdapter.fromJson(roadsterJson)

        with(roadster) {
            this.shouldNotBeNull()
            flickrImages shouldHaveSize 4
            launchDateUTC?.year shouldBe 2018
            launchDateUTC?.month shouldBe Month.FEBRUARY
            launchDateUTC?.dayOfMonth shouldBe 6
            launchMassKg shouldBe 1350.0
        }
    }

    @Test
    fun testRoadsterInfoResponse() {
        val (service, cleanup) = useMockService<RoadsterService> {
            setBody(useJSON("/sampleData/v4/roadster_info.json"))
        }

        val response = runBlocking { service.info() }
        response.shouldBeInstanceOf<NetworkResponse.Success<RoadsterInfo>>()
        response as NetworkResponse.Success

        response.body.id shouldBe "5eb75f0842fea42237d7f3f4"

        cleanup()
    }
}