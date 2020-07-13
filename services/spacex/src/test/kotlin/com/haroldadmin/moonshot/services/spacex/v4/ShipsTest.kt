package com.haroldadmin.moonshot.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import com.squareup.moshi.Moshi
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.runBlocking

internal class ShipsTest: AnnotationSpec() {

    @Test
    fun testShipModel() {
        val shipJson = useJSON("/sampleData/v4/one_ship.json")
        val shipAdapter = useJSONAdapter<Ship>()

        val ship = shipAdapter.fromJson(shipJson)

        with(ship) {
            this.shouldNotBeNull()
            name shouldBe "GO Pursuit"
            isActive shouldBe false
            launchIDs shouldHaveSize 4
            homePort shouldBe "Port Canaveral"
        }
    }

    @Test
    fun testOneShipResponse() {
        val (service, cleanup) = useMockService<ShipsService> {
            setBody(useJSON("/sampleData/v4/one_ship.json"))
        }

        val id = "5ea6ed2e080df4000697c90a"
        val response = runBlocking { service.one(id) }
        response.shouldBeInstanceOf<NetworkResponse.Success<Ship>>()
        response as NetworkResponse.Success

        response.body.id shouldBe id
    }
}