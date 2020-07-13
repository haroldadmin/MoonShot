package com.haroldadmin.moonshot.db

import com.haroldadmin.moonshot.services.spacex.v4.test.APISampleData
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class CompanyInfoTest: AnnotationSpec() {

    @Test
    fun testMapper() {
        val apiModel = APISampleData.Company.samples().first()
        val dbModel = apiModel.toDBModel()

        dbModel.id shouldBe apiModel.id
        dbModel.name shouldBe apiModel.name
        dbModel.founder shouldBe apiModel.founder
        dbModel.founded shouldBe apiModel.founded
        dbModel.employees shouldBe apiModel.employees
        dbModel.vehicles shouldBe apiModel.vehicles
        dbModel.launchSites shouldBe apiModel.launchSites
        dbModel.testSites shouldBe apiModel.testSites
        dbModel.ceo shouldBe apiModel.ceo
        dbModel.coo shouldBe apiModel.coo
        dbModel.cto shouldBe apiModel.cto
        dbModel.ctoPropulsion shouldBe apiModel.ctoPropulsion
        dbModel.valuation shouldBe apiModel.valuation
        dbModel.headquarters_address shouldBe apiModel.headquarters?.address
        dbModel.headquarters_city shouldBe apiModel.headquarters?.city
        dbModel.headquarters_state shouldBe apiModel.headquarters?.state
        dbModel.links_website shouldBe apiModel.links?.website
        dbModel.links_flickr shouldBe apiModel.links?.flickr
        dbModel.links_twitter shouldBe apiModel.links?.twitter
        dbModel.links_elonTwitter shouldBe apiModel.links?.elonTwitter
    }
}