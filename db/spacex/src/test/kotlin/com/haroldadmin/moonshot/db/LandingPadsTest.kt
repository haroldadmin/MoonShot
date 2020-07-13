package com.haroldadmin.moonshot.db

import com.haroldadmin.moonshot.services.spacex.v4.test.APISampleData
import io.kotlintest.matchers.types.shouldNotBeSameInstanceAs
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class LandingPadsTest: AnnotationSpec() {

    @Test
    fun testMapper() {
        val apiModel = APISampleData.LandingPads.samples().first()
        val dbModel = apiModel.toDBModel()

        apiModel.id shouldBe dbModel.id
        apiModel.name shouldBe dbModel.name
        apiModel.fullName shouldBe dbModel.fullName
        apiModel.status shouldBe dbModel.status
        apiModel.type shouldBe dbModel.type
        apiModel.region shouldBe dbModel.region
        apiModel.locality shouldBe dbModel.locality
        apiModel.latitude shouldBe dbModel.latitude
        apiModel.longitude shouldBe dbModel.longitude
        apiModel.landingAttempts shouldBe dbModel.landingAttempts
        apiModel.landingSuccesses shouldBe dbModel.landingSuccesses
        apiModel.wikipedia shouldBe dbModel.wikipedia
        apiModel.details shouldBe dbModel.details
        apiModel.launchIDs shouldBe dbModel.launchIDs
        apiModel.launchIDs shouldNotBeSameInstanceAs dbModel.launchIDs
    }

}