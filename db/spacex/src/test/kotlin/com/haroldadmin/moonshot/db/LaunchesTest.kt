package com.haroldadmin.moonshot.db

import com.haroldadmin.moonshot.services.spacex.v4.test.APISampleData
import io.kotlintest.matchers.types.shouldNotBeSameInstanceAs
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

internal class LaunchesTest: AnnotationSpec() {

    @Test
    fun testMapper() {
        val apiModel = APISampleData.Launches.samples().first()
        val dbModel = apiModel.toDBModel()

        dbModel.id shouldBe apiModel.id
        dbModel.flightNumber shouldBe apiModel.flightNumber
        dbModel.launchDateUTC shouldBe  apiModel.launchDateUTC
        dbModel.launchDateLocal shouldBe apiModel.launchDateLocal
        dbModel.launchDateUnix shouldBe apiModel.launchDateUnix
        dbModel.datePrecision shouldBe apiModel.datePrecision
        dbModel.staticFireDateUTC shouldBe apiModel.staticFireDateUTC
        dbModel.staticFireDateUnix shouldBe apiModel.staticFireDateUnix
        dbModel.tbd shouldBe apiModel.tbd
        dbModel.net shouldBe apiModel.net
        dbModel.window shouldBe apiModel.window
        dbModel.rocketID shouldBe apiModel.rocketID
        dbModel.success shouldBe apiModel.success
        dbModel.failures shouldBe apiModel.failures
        dbModel.failures shouldNotBeSameInstanceAs apiModel.failures
        dbModel.upcoming shouldBe apiModel.upcoming
        dbModel.details shouldBe apiModel.details
        dbModel.fairings_recovered shouldBe apiModel.fairings?.recovered
        dbModel.fairings_reused shouldBe apiModel.fairings?.reused
        dbModel.fairings_recoveryAttempt shouldBe apiModel.fairings?.recoveryAttempted
        dbModel.fairings_shipIDs shouldBe apiModel.fairings?.shipIDs
        dbModel.fairings_shipIDs?.shouldNotBeSameInstanceAs(apiModel.fairings?.shipIDs)
        dbModel.crewIDs shouldBe apiModel.crewIDs
        dbModel.crewIDs shouldNotBeSameInstanceAs apiModel.crewIDs
        dbModel.shipIDs shouldBe apiModel.shipIDs
        dbModel.shipIDs shouldNotBeSameInstanceAs apiModel.shipIDs
        dbModel.capsuleIDs shouldBe apiModel.capsuleIDs
        dbModel.capsuleIDs shouldNotBeSameInstanceAs apiModel.capsuleIDs
        dbModel.payloadIDs shouldBe apiModel.payloadIDs
        dbModel.payloadIDs shouldNotBeSameInstanceAs apiModel.payloadIDs
        dbModel.links_patch_small shouldBe apiModel.links?.patch?.small
        dbModel.links_patch_large shouldBe apiModel.links?.patch?.large
        dbModel.links_reddit_campaign shouldBe apiModel.links?.reddit?.campaign
        dbModel.links_reddit_media shouldBe apiModel.links?.reddit?.media
        dbModel.links_reddit_launch shouldBe apiModel.links?.reddit?.launch
        dbModel.links_flickr_small shouldBe apiModel.links?.flickr?.small
        dbModel.links_flickr_original shouldBe apiModel.links?.flickr?.original
        dbModel.links_presskit shouldBe apiModel.links?.presskit
        dbModel.links_webcast shouldBe apiModel.links?.webcast
        dbModel.links_youtubeID shouldBe apiModel.links?.youtubeID
        dbModel.links_article shouldBe apiModel.links?.article
        dbModel.links_wikipedia shouldBe apiModel.links?.wikipedia
        dbModel.autoUpdate shouldBe apiModel.autoUpdate
    }

    @Test
    fun testLaunchCoreMapper() {
        val launch = APISampleData.Launches.samples().first()
        val apiModel = launch.cores.first()
        val dbModel = apiModel.toDBModel(launch.id)

        dbModel.id shouldBe apiModel.id
        dbModel.flight shouldBe apiModel.flight
        dbModel.gridfins shouldBe apiModel.gridfins
        dbModel.legs shouldBe apiModel.legs
        dbModel.reused shouldBe apiModel.reused
        dbModel.landingAttempt shouldBe apiModel.landingAttempt
        dbModel.landingSuccess shouldBe apiModel.landingSuccess
        dbModel.landingType shouldBe apiModel.landingType
        dbModel.landPadID shouldBe apiModel.landpadID
        dbModel.launchID shouldBe launch.id
    }
}