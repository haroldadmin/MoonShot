package com.haroldadmin.services.spacex.v4

import com.squareup.moshi.Moshi
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class CrewModelTest: AnnotationSpec() {

    private lateinit var moshi: Moshi

    @Before
    fun setup() {
        moshi = Moshi.Builder().build()
    }

    @Test
    fun testCrewModel() {
        val crewJson = getResource("/sampleData/v4/one_crew.json").readText()
        val crewAdapter = moshi.adapter(Crew::class.java)
        val crew = crewAdapter.fromJson(crewJson)

        with(crew) {
            this.shouldNotBeNull()
            name shouldBe "Douglas Hurley"
            launchIDs shouldHaveSize 1
        }
    }

}