package com.haroldadmin.spacex_api_wrapper.v4

import com.haroldadmin.spacex_api_wrapper.getResource
import com.squareup.moshi.Moshi
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class CapsuleModelTest: AnnotationSpec() {

    private lateinit var moshi: Moshi

    @Before
    fun setup() {
        moshi = Moshi.Builder().add(ZonedDateTimeAdapter()).build()
    }

    @Test
    fun testCapsuleModel() {
        val capsuleJson = getResource("/sampledata/v4/one_capsule.json").readText()
        val capsuleAdapter = moshi.adapter(Capsule::class.java)
        val capsule = capsuleAdapter.fromJson(capsuleJson)

        with(capsule) {
            this.shouldNotBeNull()
            reuseCount shouldBe 1
            waterLandings shouldBe 1
            landLandings shouldBe 0
            launchIDs shouldHaveSize 1
            serial shouldBe "C101"
            status shouldBe CapsuleStatus.retired
            id shouldBe "5e9e2c5bf35918ed873b2664"
        }
    }
}