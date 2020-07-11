package com.haroldadmin.services.spacex.v4

import com.squareup.moshi.Moshi
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import java.time.Month

internal class RoadsterModelTest : AnnotationSpec() {

    private lateinit var moshi: Moshi

    @Before
    fun setup() {
        moshi = Moshi.Builder().add(ZonedDateTimeAdapter()).build()
    }

    @Test
    fun testRoadsterModel() {
        val roadsterJson = getResource("/sampleData/v4/roadster_info.json").readText()
        val roadsterAdapter = moshi.adapter(RoadsterInfo::class.java)
        val roadster = roadsterAdapter.fromJson(roadsterJson)

        with(roadster) {
            this.shouldNotBeNull()
            flickrImages shouldHaveSize 4
            launchDateUTC?.year shouldBe 2018
            launchDateUTC?.month shouldBe Month.FEBRUARY
            launchDateUTC?.dayOfMonth shouldBe 6
            launchMassKg shouldBe 1350.0
        }
    }

}