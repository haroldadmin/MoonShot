package com.haroldadmin.moonshot.db

import com.haroldadmin.moonshot.services.spacex.v4.test.APISampleData
import io.kotlintest.matchers.types.shouldNotBeSameInstanceAs
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class ShipsTest: AnnotationSpec() {

    @Test
    fun testMapper() {
        val apiModel = APISampleData.Ships.samples().first()
        val dbModel = apiModel.toDBModel()

        dbModel.id shouldBe apiModel.id
        dbModel.name shouldBe apiModel.name
        dbModel.model shouldBe apiModel.model
        dbModel.type shouldBe apiModel.type
        dbModel.roles shouldBe apiModel.roles
        dbModel.roles shouldNotBeSameInstanceAs apiModel.roles
        dbModel.isActive shouldBe apiModel.isActive
        dbModel.imo shouldBe apiModel.imo
        dbModel.mmsi shouldBe apiModel.mmsi
        dbModel.abs shouldBe apiModel.abs
        dbModel.clazz shouldBe apiModel.clazz
        dbModel.massKg shouldBe apiModel.massKg
        dbModel.massLbs shouldBe apiModel.massLbs
        dbModel.yearBuilt shouldBe apiModel.yearBuilt
        dbModel.homePort shouldBe  apiModel.homePort
        dbModel.speedKn shouldBe apiModel.speedKn
        dbModel.courseDeg shouldBe apiModel.courseDeg
        dbModel.latitude shouldBe apiModel.latitude
        dbModel.longitude shouldBe apiModel.longitude
        dbModel.lastAisUpdate shouldBe apiModel.lastAisUpdate
        dbModel.link shouldBe apiModel.link
        dbModel.image shouldBe apiModel.image
        dbModel.launchIDs shouldBe apiModel.launchIDs
        dbModel.launchIDs shouldNotBeSameInstanceAs apiModel.launchIDs
    }
}