package com.haroldadmin.spacex_api_wrapper.payload

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.BaseApiTest
import com.haroldadmin.spacex_api_wrapper.enqueue
import com.haroldadmin.spacex_api_wrapper.fromFile
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe

internal class PayloadTest : BaseApiTest() {

    private val service by lazy { retrofit.create(PayloadsService::class.java) }

    init {
        describe("Payload Service") {
            context("All payloads request") {
                server.enqueue { fromFile("/sampledata/payloads/all_payloads.json") }
                val response = service.getAllPayloads().await()

                it("Should return successfully") {
                    (response is NetworkResponse.Success) shouldBe true
                }

                it("Should parse sample data correctly") {
                    (response as NetworkResponse.Success).body shouldHaveSize 79
                }
            }

            context("One payload request") {
                server.enqueue { fromFile("/sampledata/payloads/one_payload.json") }
                val payloadId = "Telkom-4"
                val response = service.getPayload(payloadId).await()

                it("Should return successfully") {
                    (response is NetworkResponse.Success) shouldBe true
                }

                it("Should parse sample data correctly") {
                    (response as NetworkResponse.Success).body.id shouldBe payloadId
                }
            }
        }
    }
}