package com.haroldadmin.spacex_api_wrapper.launch

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.getResource
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import com.haroldadmin.spacex_api_wrapper.success
import com.haroldadmin.spacex_api_wrapper.testModule
import io.kotlintest.Spec
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.get
import retrofit2.Retrofit

class LaunchTest : KoinTest, DescribeSpec() {

    private val server: MockWebServer by lazy { MockWebServer() }
    private val service: LaunchesService by lazy {
        get<Retrofit> { parametersOf(server) }.create(LaunchesService::class.java)
    }

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        startKoin {
            modules(testModule)
        }
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        stopKoin()
        server.close()
    }

    init {
        describe("Launch service") {

            context("Next Launch request") {
                server.enqueue(
                    MockResponse().success("/sampledata/launches/next_launch.json")
                )

                val response = service.getNextLaunch().await()

                it("Should return the next launch successfully") {
                    (response is NetworkResponse.Success) shouldBe true
                }

                it("Should have a flight number same as the sample data") {
                    (response as NetworkResponse.Success).body.flightNumber shouldBe 75
                }
            }

            context("One launch request") {

                server.enqueue(
                    MockResponse().success("/sampledata/launches/one_launch.json")
                )
                val flightNumber = 65
                val response = service.getLaunch(flightNumber).await()

                it ("Should return the requested launch successfully") {
                    (response is NetworkResponse.Success) shouldBe true
                }

                it ("Should return the same launch as the sample data") {
                    (response as NetworkResponse.Success).body.flightNumber shouldBe flightNumber
                }
            }
        }
    }

}