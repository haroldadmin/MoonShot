package com.haroldadmin.spacex_api_wrapper.missions

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.BaseApiTest
import com.haroldadmin.spacex_api_wrapper.enqueue
import com.haroldadmin.spacex_api_wrapper.fromFile
import com.haroldadmin.spacex_api_wrapper.mission.MissionService
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe

internal class MissionsTest : BaseApiTest() {

    private val service by lazy { retrofit.create(MissionService::class.java) }

    init {
        describe("Missions Service") {
            context("All missions request") {
                server.enqueue { fromFile("/sampledata/missions/all_missions.json") }
                val response = service.getAllMissions().await()

                it("Should return successfully") {
                    (response is NetworkResponse.Success) shouldBe true
                }

                it("Should parse sample data correctly") {
                    (response as NetworkResponse.Success).body shouldHaveSize 4
                }
            }

            context("One mission request") {
                server.enqueue { fromFile("/sampledata/missions/one_mission.json") }
                val missionId = "F3364BF"
                val response = service.getMission(missionId).await()

                it("Should return successfully") {
                    (response is NetworkResponse.Success) shouldBe true
                }

                it("Should parse sample data correctly") {
                    (response as NetworkResponse.Success).body.id shouldBe missionId
                }
            }
        }
    }
}