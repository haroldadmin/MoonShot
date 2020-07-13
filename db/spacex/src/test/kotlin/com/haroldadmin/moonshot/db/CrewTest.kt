package com.haroldadmin.moonshot.db

import com.haroldadmin.moonshot.services.spacex.v4.test.APISampleData
import io.kotlintest.matchers.types.shouldNotBeSameInstanceAs
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class CrewTest: AnnotationSpec() {

    @Test
    fun mapperTest() {
        val apiModel = APISampleData.Crews.samples().first()
        val dbModel = apiModel.toDBModel()

        dbModel.id shouldBe apiModel.id
        dbModel.name shouldBe apiModel.name
        dbModel.status shouldBe apiModel.status
        dbModel.agency shouldBe apiModel.agency
        dbModel.image shouldBe apiModel.image
        dbModel.wikipedia shouldBe apiModel.wikipedia
        dbModel.launchIDs shouldBe apiModel.launchIDs
        dbModel.launchIDs shouldNotBeSameInstanceAs apiModel.launchIDs
    }

}