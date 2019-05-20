package com.haroldadmin.spacex_api_wrapper.rockets

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.BaseApiTest
import com.haroldadmin.spacex_api_wrapper.enqueue
import com.haroldadmin.spacex_api_wrapper.fromFile
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe

internal class RocketsTest : BaseApiTest() {
    private val service by lazy { retrofit.create(RocketsService::class.java) }

    init {
        describe("Rockets service") {
            context("All rockets request") {
                server.enqueue { fromFile("/sampledata/rockets/all_rockets.json") }
                val response = service.getAllRockets().await()

                it("Should return successfully") {
                    (response is NetworkResponse.Success) shouldBe true
                }

                it("Should parse sampledata correctly") {
                    (response as NetworkResponse.Success).body shouldHaveSize 4
                }
            }

            context("One rocket request") {
                server.enqueue { fromFile("/sampledata/rockets/one_rocket.json") }
                val rocketId = "falcon9"
                val response = service.getRocket(rocketId).await()

                it("Should return successfully") {
                    (response is NetworkResponse.Success) shouldBe true
                }

                it("Should parse sampledata correctly") {
                    (response as NetworkResponse.Success).body.rockedId shouldBe rocketId
                }
            }
        }
    }
}