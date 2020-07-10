package com.haroldadmin.spacex_api_wrapper.v4

import com.haroldadmin.spacex_api_wrapper.getResource
import com.squareup.moshi.Moshi
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class LaunchPadModelTest: AnnotationSpec() {

    private lateinit var moshi: Moshi

    @Before
    fun setup() {
        moshi = Moshi.Builder().build()
    }

    @Test
    fun testLaunchPadModel() {
        val launchPadJson = getResource("/sampledata/v4/one_launchpad.json").readText()
        val launchPadAdapter = moshi.adapter(LaunchPad::class.java)
        val launchPad = launchPadAdapter.fromJson(launchPadJson)

        with(launchPad) {
            this.shouldNotBeNull()
            name shouldBe "VAFB SLC 4E"
            launchIDs shouldHaveSize 15
        }
    }

}