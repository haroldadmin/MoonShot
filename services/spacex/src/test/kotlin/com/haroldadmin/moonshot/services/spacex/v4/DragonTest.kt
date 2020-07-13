package com.haroldadmin.moonshot.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.runBlocking

internal class DragonTest: AnnotationSpec() {

    @Test
    fun testDragonModel() {
        val dragonJson = useJSON("/sampleData/v4/one_dragon.json")
        val dragonAdapter = useJSONAdapter<Dragon>()
        val dragon = dragonAdapter.fromJson(dragonJson)

        with(dragon) {
            this.shouldNotBeNull()
            heatShield.material.shouldBe("PICA-X")

            launchPayloadMass?.kg shouldBe 6000.0
            launchPayloadVolume?.cubicMetres shouldBe 25.0
            returnPayloadMass?.kg shouldBe 3000.0
            returnPayloadVolume?.cubicMetres shouldBe 11.0
            pressurizedCapsule?.payloadVolume?.cubicMetres shouldBe 11.0

            trunk?.trunkVolume?.cubicMetres shouldBe 14.0
            trunk?.cargo?.solarArray shouldBe 2
            trunk?.cargo?.unpressurizedCargo shouldBe true

            heightWithTrunk?.metres shouldBe 7.2
            diameter?.metres shouldBe 3.7

            firstFlight?.year shouldBe 2010

            flickrImages shouldHaveSize 4

            thrusters shouldHaveSize 1
            thrusters.first().type shouldBe "Draco"

        }
    }

    @Test
    fun testAllDragonsResponse() {
        val (service, cleanup) = useMockService<DragonsService> {
            setBody(useJSON("/sampleData/v4/all_dragons.json"))
        }

        val response = runBlocking { service.all() }
        response.shouldBeInstanceOf<NetworkResponse.Success<List<Dragon>>>()
        response as NetworkResponse.Success

        response.body shouldHaveSize 1

        cleanup()
    }

    @Test
    fun testOneDragonResponse() {
        val (service, cleanup) = useMockService<DragonsService> {
            setBody(useJSON("/sampleData/v4/one_dragon.json"))
        }

        val id = "5e9d058759b1ff74a7ad5f8f"
        val response = runBlocking { service.one(id) }
        response.shouldBeInstanceOf<NetworkResponse.Success<Dragon>>()
        response as NetworkResponse.Success

        response.body.id shouldBe id
    }

}
