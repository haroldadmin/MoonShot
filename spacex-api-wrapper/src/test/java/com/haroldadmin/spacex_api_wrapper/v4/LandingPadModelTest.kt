package com.haroldadmin.spacex_api_wrapper.v4

import com.haroldadmin.spacex_api_wrapper.getResource
import com.squareup.moshi.Moshi
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class LandingPadModelTest: AnnotationSpec() {

    private lateinit var moshi: Moshi

    @Before
    fun setup() {
        moshi = Moshi.Builder().add(LocalDateAdapter()).build()
    }

    @Test
    fun testLandingPadModel() {
        val landingPadJson = getResource("/sampledata/v4/one_landingpad.json").readText()
        val landingPadAdapter = moshi.adapter(LandingPad::class.java)
        val landingPad = landingPadAdapter.fromJson(landingPadJson)

        with(landingPad) {
            this.shouldNotBeNull()
            name shouldBe "LZ-2"
            status shouldBe "active"
            launchIDs shouldHaveSize 3
        }
    }

}