package com.haroldadmin.spacex_api_wrapper.launch

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.BaseApiTest
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import com.haroldadmin.spacex_api_wrapper.fromFile
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

internal class LaunchTest : BaseApiTest() {

    private val service: LaunchesService by lazy { retrofit.create(LaunchesService::class.java) }

    init {
        describe("Launch service") {

            context("Next Launch request") {
                server.enqueue(
                    MockResponse().fromFile("/sampledata/launches/next_launch.json")
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
                    MockResponse().fromFile("/sampledata/launches/one_launch.json")
                )
                val flightNumber = 65
                val response = service.getLaunch(flightNumber).await()

                it("Should return the requested launch successfully") {
                    (response is NetworkResponse.Success) shouldBe true
                }

                it("Should return the same launch as the sample data") {
                    (response as NetworkResponse.Success).body.flightNumber shouldBe flightNumber
                }
            }

            context("All launches request") {
                server.enqueue(
                    MockResponse().fromFile("/sampledata/launches/all_launches.json")
                )

                val response = service.getAllLaunches().await()

                it("Should return a list of launches") {
                    (response is NetworkResponse.Success) shouldBe true
                    (response as NetworkResponse.Success).body.size shouldBe 92
                }
            }

            context("Upcoming launches request") {
                server.enqueue(
                    MockResponse().fromFile("/sampledata/launches/upcoming_launches.json")
                )

                val response = service.getUpcomingLaunches().await()

                it("Should return a list of launches") {
                    (response is NetworkResponse.Success) shouldBe true
                    (response as NetworkResponse.Success).body.size shouldBe 18
                }
            }

            context("Latest launch request") {
                server.enqueue(
                    MockResponse().fromFile("/sampledata/launches/latest_launch.json")
                )

                val response = service.getLatestLaunch().await()

                it("Should return a launch object") {
                    (response is NetworkResponse.Success) shouldBe true
                    (response as NetworkResponse.Success).body.flightNumber shouldBe 74
                }
            }

            context("Past launches request") {
                server.enqueue(
                    MockResponse().fromFile("/sampledata/launches/past_launches.json")
                )

                val response = service.getPastLaunches().await()

                it("Should return a list of launches") {
                    (response is NetworkResponse.Success) shouldBe true
                    (response as NetworkResponse.Success).body.size shouldBe 74
                }
            }
        }
    }

}