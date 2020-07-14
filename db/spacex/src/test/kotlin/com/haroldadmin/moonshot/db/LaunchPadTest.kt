package com.haroldadmin.moonshot.db

import com.haroldadmin.moonshot.services.spacex.v4.test.APISampleData
import io.kotlintest.matchers.types.shouldNotBeSameInstanceAs
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class LaunchPadTest: AnnotationSpec() {

    @Test
    fun mapperTest() {
        val apiModel = APISampleData.LaunchPads.samples().first()
        val dbModel = apiModel.toDBModel()

        dbModel.id shouldBe apiModel.id
        dbModel.name shouldBe apiModel.name
        dbModel.fullName shouldBe apiModel.fullName
        dbModel.status shouldBe apiModel.status
        dbModel.locality shouldBe apiModel.locality
        dbModel.region shouldBe apiModel.region
        dbModel.timezone shouldBe apiModel.timezone
        dbModel.latitude shouldBe apiModel.latitude
        dbModel.longitude shouldBe apiModel.longitude
        dbModel.launchAttempts shouldBe apiModel.launchAttempts
        dbModel.launchSuccesses shouldBe apiModel.launchSuccesses
        dbModel.rocketIDs shouldBe apiModel.rocketIDs
        dbModel.rocketIDs shouldNotBeSameInstanceAs  apiModel.rocketIDs
        dbModel.launchIDs shouldBe apiModel.launchIDs
        dbModel.launchIDs shouldNotBeSameInstanceAs apiModel.launchIDs
    }

}