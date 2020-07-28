package com.haroldadmin.moonshot.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.services.spacex.v4.test.useJSON
import com.haroldadmin.moonshot.services.spacex.v4.test.useJSONAdapter
import com.haroldadmin.moonshot.services.spacex.v4.test.useMockService
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.runBlocking

internal class CompanyInfoTest: AnnotationSpec() {

    @Test
    fun testCompanyInfoModel() {
        val companyInfoAdapter =
            useJSONAdapter<CompanyInfo>()
        val companyInfoJson =
            useJSON("/sampleData/v4/company_info.json")
        val companyInfo = companyInfoAdapter.fromJson(companyInfoJson)

        with(companyInfo) {
            this.shouldNotBeNull()
            id shouldBe "5eb75edc42fea42237d7f3ed"
            name shouldBe "SpaceX"
            headquarters.shouldNotBeNull()
            links.shouldNotBeNull()
        }
    }

    @Test
    fun testCompanyInfoResponse() {

        val (service, cleanup) = useMockService<CompanyInfoService> {
            setBody(com.haroldadmin.moonshot.services.spacex.v4.test.useJSON("/sampleData/v4/company_info.json"))
        }

        val response = runBlocking { service.info() }
        response.shouldBeInstanceOf<NetworkResponse.Success<CompanyInfo>>()
        response as NetworkResponse.Success

        response.body.name shouldBe "SpaceX"

        cleanup()
    }

}