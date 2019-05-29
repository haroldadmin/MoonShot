package com.haroldadmin.spacex_api_wrapper.historical_events

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.BaseApiTest
import com.haroldadmin.spacex_api_wrapper.enqueue
import com.haroldadmin.spacex_api_wrapper.fromFile
import com.haroldadmin.spacex_api_wrapper.history.HistoryService
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe

internal class HistoricalEventsTest : BaseApiTest() {

    private val service by lazy { retrofit.create(HistoryService::class.java) }

    init {
        describe("History Service") {

            context("All historical events") {
                server.enqueue { fromFile("/sampledata/historical_events/all_historical_events.json") }
                val response = service.getAllHistoricalEvents().await()

                it("Should return successfully") {
                    (response is NetworkResponse.Success) shouldBe true
                }

                it("Should parse the sample data correctly") {
                    (response as NetworkResponse.Success).body shouldHaveSize 20
                }
            }

            context("One historical event") {
                server.enqueue { fromFile("/sampledata/historical_events/one_historical_event.json") }
                val id = 1
                val response = service.getHistoricalEvent(1).await()

                it("Should return successfully") {
                    (response is NetworkResponse.Success) shouldBe true
                }

                it("Should parse the sample data correctly") {
                    (response as NetworkResponse.Success).body.id shouldBe id
                }
            }
        }
    }
}