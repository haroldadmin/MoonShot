package com.haroldadmin.moonshot.db

import com.haroldadmin.moonshot.services.spacex.v4.test.APISampleData
import io.kotlintest.matchers.types.shouldNotBeSameInstanceAs
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class RoadsterTest: AnnotationSpec() {

    @Test
    fun testMapper() {
        val apiModel = APISampleData.Roadster.samples().first()
        val dbModel = apiModel.toDBModel()

        dbModel.id shouldBe apiModel.id
        dbModel.name shouldBe apiModel.name
        dbModel.launchDateUTC shouldBe apiModel.launchDateUTC
        dbModel.launchDateUnix shouldBe apiModel.launchDateUnix
        dbModel.launchMassKg shouldBe apiModel.launchMassKg
        dbModel.launchMassLbs shouldBe apiModel.launchMassLbs
        dbModel.noradID shouldBe apiModel.noradID
        dbModel.epochJD shouldBe apiModel.epochJd
        dbModel.orbitType shouldBe apiModel.orbitType
        dbModel.apoapsisAu shouldBe apiModel.apoapsisAu
        dbModel.periapsisArg shouldBe apiModel.periapsisArg
        dbModel.semiMajorAxisAu shouldBe apiModel.semiMajorAxisAu
        dbModel.eccentricity shouldBe apiModel.eccentricity
        dbModel.inclination shouldBe apiModel.inclination
        dbModel.longitude shouldBe apiModel.longitude
        dbModel.periapsisArg shouldBe  apiModel.periapsisArg
        dbModel.periodDays shouldBe apiModel.periodDays
        dbModel.speedKph shouldBe apiModel.speedKph
        dbModel.speedMph shouldBe apiModel.speedMph
        dbModel.earthDistanceKm shouldBe apiModel.earthDistanceKm
        dbModel.earthDistanceMi shouldBe apiModel.earthDistanceMiles
        dbModel.marsDistanceKm shouldBe apiModel.marsDistanceKm
        dbModel.marsDistanceMi shouldBe apiModel.marsDistanceMiles
        dbModel.flickrImages shouldBe apiModel.flickrImages
        dbModel.flickrImages shouldNotBeSameInstanceAs  apiModel.flickrImages
        dbModel.wikipedia shouldBe apiModel.wikipedia
        dbModel.video shouldBe apiModel.video
        dbModel.details shouldBe apiModel.details
    }

}