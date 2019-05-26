package com.haroldadmin.spacex_api_wrapper.capsule

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.BaseApiTest
import com.haroldadmin.spacex_api_wrapper.enqueue
import com.haroldadmin.spacex_api_wrapper.fromFile
import com.haroldadmin.spacex_api_wrapper.launches.Launch
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe

internal class CapsuleTest: BaseApiTest() {

    private val service by lazy { retrofit.create(CapsuleService::class.java) }

    init {

        describe("Capsules Service") {
            context("All capsules request") {
                server.enqueue { fromFile("/sampledata/capsules/all_capsules_response.json") }
                val response = service.getAllCapsules().await()

                it ("Should return successfully") {
                    (response is NetworkResponse.Success) shouldBe true
                }

                it ("Should parse sample data correctly") {
                    (response as NetworkResponse.Success).body shouldHaveSize 18
                }
            }

            context("One capsule request") {
                server.enqueue { fromFile("/sampledata/capsules/one_capsule_response.json") }
                val serial = "C112"
                val response = service.getCapsule(serial).await()

                it("Should return successfully") {
                    response.shouldBeTypeOf<NetworkResponse.Success<Launch>>()
                }

                it("Should parse sample data correctly") {
                    (response as NetworkResponse.Success).body.serial shouldBe serial
                }
            }
        }

    }

}
