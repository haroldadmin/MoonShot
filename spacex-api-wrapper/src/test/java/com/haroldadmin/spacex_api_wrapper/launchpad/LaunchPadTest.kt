package com.haroldadmin.spacex_api_wrapper.launchpad

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.BaseApiTest
import com.haroldadmin.spacex_api_wrapper.enqueue
import com.haroldadmin.spacex_api_wrapper.fromFile
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe

internal class LaunchPadTest: BaseApiTest() {

    private val service by lazy { retrofit.create(LaunchPadService::class.java) }

    init {

        describe("LaunchPad Service") {
            context("All launchpads request") {
                server.enqueue { fromFile("/sampledata/launch_pads/all_launch_pads.json") }

                val response = service.getAllLaunchPads().await()

                it ("Should return successfully") {
                    (response is NetworkResponse.Success) shouldBe true
                }

                it ("Should parse sample data correctly") {
                    (response as NetworkResponse.Success).body shouldHaveSize 6
                }
            }

            context("One launchpad request") {
                val siteId = "vafb_slc_4e"
                server.enqueue { fromFile("/sampledata/launch_pads/one_launch_pad.json") }
                val response = service.getLaunchPad(siteId).await()

                it ("Should return successfully") {
                    (response is NetworkResponse.Success) shouldBe true
                }

                it ("Should parse sample data correctly") {
                    (response as NetworkResponse.Success).body.siteId shouldBe siteId
                }
            }
        }

    }
}