package com.haroldadmin.services.spacex.v4

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class DragonModelTest: AnnotationSpec() {

    private lateinit var moshi: Moshi

    @Before
    fun setup() {
        moshi = Moshi.Builder()
            .add(LocalDateAdapter())
            .build()
    }

    @Test
    fun testDragonModel() {
        val dragonJson = getResource("/sampleData/v4/one_dragon.json").readText()
        val dragonAdapter = moshi.adapter(Dragon::class.java)
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

}
