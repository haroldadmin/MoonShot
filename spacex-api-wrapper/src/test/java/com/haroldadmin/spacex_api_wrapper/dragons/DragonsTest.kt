package com.haroldadmin.spacex_api_wrapper.dragons

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.dragon.DragonsService
import com.haroldadmin.spacex_api_wrapper.enqueue
import com.haroldadmin.spacex_api_wrapper.fromFile
import com.haroldadmin.spacex_api_wrapper.testModule
import io.kotlintest.Spec
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.inject
import retrofit2.Retrofit

class DragonsTest : KoinTest, DescribeSpec() {

    private val server by lazy { MockWebServer() }
    private val retrofit by inject<Retrofit> { parametersOf(server) }
    private val service by lazy { retrofit.create(DragonsService::class.java) }

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        startKoin {
            modules(testModule)
        }
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        stopKoin()
    }

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