package com.haroldadmin.spacex_api_wrapper.v4

import com.haroldadmin.spacex_api_wrapper.getResource
import com.squareup.moshi.Moshi
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import java.time.Month

internal class PayloadModelTest: AnnotationSpec() {

    private lateinit var moshi: Moshi

    @Before
    fun setup() {
        moshi = Moshi.Builder()
            .add(ZonedDateTimeAdapter())
            .build()
    }

    @Test
    fun testPayloadModel() {
        val payloadJson = getResource("/sampledata/v4/one_payload.json").readText()
        val payloadAdapter = moshi.adapter(Payload::class.java)
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

}