package com.haroldadmin.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.runBlocking
import java.time.Month

internal class PayloadTest: AnnotationSpec() {

    @Test
    fun testPayloadModel() {
        val payloadJson = useJSON("/sampleData/v4/one_payload.json")
        val payloadAdapter = useJSONAdapter<Payload>()
        val payload = payloadAdapter.fromJson(payloadJson)

        with(payload) {
            this.shouldNotBeNull()
            name shouldBe "Tintin A & B"
            dragon.shouldNotBeNull()
            epoch?.year shouldBe 2020
            epoch?.month shouldBe Month.JUNE
            epoch?.dayOfMonth shouldBe 13
        }
    }

    @Test
    fun testOnePayloadResponse() {
        val (service, cleanup) = useMockService<PayloadService> {
            setBody(useJSON("/sampleData/v4/one_payload.json"))
        }

        val id = "5eb0e4c6b6c3bb0006eeb21e"
        val response = runBlocking { service.one(id) }
        response.shouldBeInstanceOf<NetworkResponse.Success<Payload>>()
        response as NetworkResponse.Success

        response.body.id shouldBe id

        cleanup()
    }
}