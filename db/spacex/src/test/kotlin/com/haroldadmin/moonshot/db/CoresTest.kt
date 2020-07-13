package com.haroldadmin.moonshot.db

import com.haroldadmin.moonshot.services.spacex.v4.test.APISampleData
import io.kotlintest.matchers.types.shouldNotBeSameInstanceAs
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class CoresTest: AnnotationSpec() {

    @Test
    fun testMapper() {
        val apiModel = APISampleData.Cores.samples().first()
        val dbModel = apiModel.toDBModel()

        dbModel.id shouldBe apiModel.id
        dbModel.serial shouldBe apiModel.serial
        dbModel.block shouldBe apiModel.block
        dbModel.status shouldBe apiModel.status
        dbModel.reuseCount shouldBe apiModel.reuseCount
        dbModel.rtlsAttempts shouldBe apiModel.rtlsAttempts
        dbModel.rtlsLandings shouldBe apiModel.rtlsLandings
        dbModel.asdsAttempts shouldBe apiModel.asdsAttempts
        dbModel.asdsLandings shouldBe dbModel.asdsLandings
        dbModel.lastUpdate shouldBe dbModel.lastUpdate
        dbModel.launchIDs shouldBe apiModel.launchIDs
        dbModel.launchIDs shouldNotBeSameInstanceAs apiModel.launchIDs
    }

}