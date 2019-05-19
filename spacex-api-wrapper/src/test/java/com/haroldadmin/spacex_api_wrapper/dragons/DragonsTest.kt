package com.haroldadmin.spacex_api_wrapper.dragons

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.BaseApiTest
import com.haroldadmin.spacex_api_wrapper.dragon.DragonsService
import com.haroldadmin.spacex_api_wrapper.enqueue
import com.haroldadmin.spacex_api_wrapper.fromFile
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe

class DragonsTest : BaseApiTest() {

    private val service by lazy { retrofit.create(DragonsService::class.java) }

    init {

        describe("Dragons service") {

            context("All dragons") {
                server.enqueue{ fromFile("/sampledata/dragons/all_dragons.json") }
                val response = service.getAllDragons().await()

                it("Should return a succesful response") {
                    (response is NetworkResponse.Success) shouldBe true
                }

                it("Should return as many elements as there are in the sample data file") {
                    (response as NetworkResponse.Success).body shouldHaveSize 2
                }
            }

            context("One dragon") {
                server.enqueue { fromFile("/sampledata/dragons/one_dragon.json") }
                val dragonId = "dragon1"
                val response = service.getDragon(dragonId).await()

                it("Should return a successful response") {
                    (response is NetworkResponse.Success) shouldBe true
                }

                it("Sample data should have been parsed correctly") {
                    (response as NetworkResponse.Success).body.id shouldBe dragonId
                }
            }
        }

    }

}