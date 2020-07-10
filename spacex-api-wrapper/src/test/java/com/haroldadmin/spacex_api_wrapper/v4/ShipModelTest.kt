package com.haroldadmin.spacex_api_wrapper.v4

import com.haroldadmin.spacex_api_wrapper.getResource
import com.squareup.moshi.Moshi
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class ShipModelTest: AnnotationSpec() {

    private lateinit var moshi: Moshi

    @Before
    fun setup() {
        moshi = Moshi.Builder().build()
    }

    @Test
    fun testShipModel() {
        val shipJson = getResource("/sampledata/v4/one_ship.json").readText()
        val shipAdapter = moshi.adapter(Ship::class.java)
        val ship = shipAdapter.fromJson(shipJson)

        with(ship) {
            this.shouldNotBeNull()
            name shouldBe "GO Pursuit"
            isActive shouldBe false
            launchIDs shouldHaveSize 4
            homePort shouldBe "Port Canaveral"
        }
    }

}