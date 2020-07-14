package com.haroldadmin.moonshot.db

import com.haroldadmin.moonshot.services.spacex.v4.test.APISampleData
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class Payloadtest: AnnotationSpec() {

    @Test
    fun testMapper() {
        val apiModel = APISampleData.Payloads.samples().first()
        val dbModel = apiModel.toDBModel()

        dbModel.id shouldBe apiModel.id
        dbModel.name shouldBe apiModel.name
        dbModel.type shouldBe apiModel.type
        dbModel.launchID shouldBe apiModel.launchID
        dbModel.customers shouldBe apiModel.customers
        dbModel.noradIDs shouldBe apiModel.noradIDs
        dbModel.nationalities shouldBe apiModel.nationalities
        dbModel.manufacturers shouldBe apiModel.manufacturers
        dbModel.massKg shouldBe apiModel.massKg
        dbModel.massLbs shouldBe apiModel.massLbs
        dbModel.orbit shouldBe apiModel.orbit
        dbModel.referenceSystem shouldBe apiModel.referenceSystem
        dbModel.regime shouldBe apiModel.regime
        dbModel.longitude shouldBe  apiModel.longitude
        dbModel.semiMajorAxisKm shouldBe apiModel.semiMajorAxisKm
        dbModel.eccentricity shouldBe apiModel.eccentricity
        dbModel.periapsisKm shouldBe apiModel.periapsisKm
        dbModel.inclinationDeg shouldBe  apiModel.inclinationDeg
        dbModel.periodMin shouldBe apiModel.periodMin
        dbModel.lifespanYears shouldBe apiModel.lifespanYears
        dbModel.epoch shouldBe apiModel.epoch
        dbModel.meanMotion shouldBe apiModel.meanMotion
        dbModel.raan shouldBe apiModel.raan
        dbModel.argOfPericentre shouldBe apiModel.argOfPericentre
        dbModel.meanAnomaly shouldBe apiModel.meanAnomaly
        dbModel.reused shouldBe apiModel.reused
        dbModel.dragon_capsuleID shouldBe apiModel.dragon?.capsuleID
        dbModel.dragon_massReturnedKg shouldBe apiModel.dragon?.massReturnedKg
        dbModel.dragon_massReturnedLbs shouldBe apiModel.dragon?.massReturnedLbs
        dbModel.dragon_flightTimeSec shouldBe apiModel.dragon?.flightTimeSec
        dbModel.dragon_manifest shouldBe apiModel.dragon?.manifest
        dbModel.dragon_waterLanding shouldBe apiModel.dragon?.waterLanding
        dbModel.dragon_landLanding shouldBe apiModel.dragon?.landLanding
    }

}