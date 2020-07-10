package com.haroldadmin.spacex_api_wrapper.v4

import com.haroldadmin.spacex_api_wrapper.getResource
import com.squareup.moshi.Moshi
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class CompanyInfoModelTest: AnnotationSpec() {

    private lateinit var moshi: Moshi

    @Before
    fun setup() {
        moshi = Moshi.Builder().build()
    }

    @Test
    fun testCompanyInfoModel() {
        val companyInfoJson = getResource("/sampledata/v4/company_info.json").readText()
        val companyInfoAdapter = moshi.adapter(CompanyInfo::class.java)
        val companyInfo = companyInfoAdapter.fromJson(companyInfoJson)

        with(companyInfo) {
            this.shouldNotBeNull()
            id shouldBe "5eb75edc42fea42237d7f3ed"
            name shouldBe "SpaceX"
            headquarters.shouldNotBeNull()
            links.shouldNotBeNull()
        }
    }

}