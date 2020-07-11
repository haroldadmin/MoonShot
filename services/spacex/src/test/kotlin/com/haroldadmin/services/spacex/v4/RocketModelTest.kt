package com.haroldadmin.services.spacex.v4

import com.squareup.moshi.Moshi
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class RocketModelTest: AnnotationSpec() {

    private lateinit var moshi: Moshi

    @Before
    fun setup() {
        moshi = Moshi.Builder().build()
    }

    @Test
    fun testRocketModel() {
        val rocketJson = getResource("/sampleData/v4/one_rocket.json").readText()
        val rocketAdapter = moshi.adapter(Rocket::class.java)
        val rocket = rocketAdapter.fromJson(rocketJson)

        with(rocket) {
            this.shouldNotBeNull()

            height?.metres shouldBe 70.0
            diameter?.metres shouldBe 12.2
            mass?.kg shouldBe 1420788.0

            firstStage?.thrustSeaLevel?.kN shouldBe 22819.0
            firstStage?.thrustVacuum?.kN shouldBe 24681.0
            firstStage?.reusable shouldBe true

            secondStage?.thrust?.kN shouldBe 934.0
            secondStage?.payloads?.compositeFairing?.height?.metres shouldBe 13.1
            secondStage?.payloads?.compositeFairing?.diameter?.metres shouldBe 5.2
            secondStage?.payloads?.optionOne shouldBe "dragon"

            engines?.isp?.seaLevel shouldBe 288.0
            engines?.thrustSeaLevel?.kN shouldBe 845.0
            engines?.thrustVacuum?.kN shouldBe 914.0
            engines?.layout shouldBe "octaweb"

            landingLegs?.number shouldBe 12
            payloadWeights shouldHaveSize 4
            flickrImages shouldHaveSize 4

            id shouldBe "5e9d0d95eda69974db09d1ed"
        }
    }

}