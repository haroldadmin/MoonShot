package com.haroldadmin.spacex_api_wrapper.v4

import com.haroldadmin.spacex_api_wrapper.getResource
import com.squareup.moshi.Moshi
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class CoresModelTest: AnnotationSpec() {

    private lateinit var moshi: Moshi

    @Before
    fun setup() {
        moshi = Moshi.Builder().build()
    }

    @Test
    fun testCoreModel() {
        val coreJson = getResource("/sampledata/v4/one_core.json").readText()
        val coreAdapter = moshi.adapter(Core::class.java)
        val core = coreAdapter.fromJson(coreJson)

        with(core) {
            this.shouldNotBeNull()
            id shouldBe "5e9e28a6f35918c0803b265c"
            launchIDs shouldHaveSize 4
        }
    }
}