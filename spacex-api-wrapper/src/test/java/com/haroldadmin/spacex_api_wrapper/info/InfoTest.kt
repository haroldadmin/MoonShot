package com.haroldadmin.spacex_api_wrapper.info

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.BaseApiTest
import com.haroldadmin.spacex_api_wrapper.enqueue
import com.haroldadmin.spacex_api_wrapper.fromFile
import io.kotlintest.matchers.numerics.shouldBeGreaterThan
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.shouldBe

class InfoTest: BaseApiTest() {
    private val service by lazy { retrofit.create(InfoService::class.java) }

    init {
        describe("Info service") {
            context("Company info") {
                server.enqueue { fromFile("/sampledata/info/company_info.json") }
                val response = service.getSpacexInfo().await()

                it("Should return successfully") {
                    (response is NetworkResponse.Success) shouldBe true
                }

                it("Should parse sample data correctly") {
                    with((response as NetworkResponse.Success).body) {
                        name shouldContain "SpaceX"
                        ceo shouldContain "Elon Musk"
                        cto shouldContain "Elon Musk"
                        coo shouldContain "Gwynne Shotwell"
                        foundedYear shouldBe 2002
                        employees shouldBe 7000
                        vehicles shouldBe 3
                        launchSites shouldBe 3
                        testSites shouldBe 1
                        valuation shouldBeGreaterThan 0
                    }
                }
            }
        }
    }
}