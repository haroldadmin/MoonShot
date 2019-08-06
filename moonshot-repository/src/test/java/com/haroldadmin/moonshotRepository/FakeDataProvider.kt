package com.haroldadmin.moonshotRepository

import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.launch.toLaunchMinimal
import com.haroldadmin.moonshot.models.launch.Launch as DbLaunch
import com.haroldadmin.spacex_api_wrapper.launches.Fairing
import com.haroldadmin.spacex_api_wrapper.launches.FirstStageSummary
import com.haroldadmin.spacex_api_wrapper.launches.Launch
import com.haroldadmin.spacex_api_wrapper.launches.LaunchSite
import com.haroldadmin.spacex_api_wrapper.launches.Links
import com.haroldadmin.spacex_api_wrapper.launches.RocketSummary
import com.haroldadmin.spacex_api_wrapper.launches.SecondStageSummary
import com.haroldadmin.spacex_api_wrapper.launches.Telemetry
import com.haroldadmin.spacex_api_wrapper.launches.Timeline
import java.util.Date

object FakeDataProvider {

    fun getDbLaunches(number: Int): List<LaunchMinimal> {
        return generateSequence { DbLaunch.getSampleLaunch() }
            .map { it.toLaunchMinimal() }
            .take(number)
            .toList()
    }

    fun getApiLaunches(number: Int): List<Launch> {
        var flightNumber = 1
        return generateSequence {
            Launch(
                flightNumber = flightNumber++,
                missionName = "Test Launch",
                missionId = listOf("Test Mission ID"),
                launchYear = "1970",
                launchDate = Date(),
                isTentative = false,
                tentativeMaxPrecision = "Test Precision",
                tbd = false,
                details = "Test Details",
                launchSuccess = false,
                launchSite = testLaunchSite(),
                launchWindow = 0,
                links = testLinks(),
                rocket = testRocketSummary(),
                ships = listOf(),
                staticFireDate = Date(),
                telemetry = testTelemetry(),
                timeline = testTimeline(),
                upcoming = false
            )
        }
            .take(number)
            .toList()
    }
}

private fun testLaunchSite(): LaunchSite {
    return LaunchSite(id = "Test ID", name = "Test Name", nameLong = "Test Name")
}

private fun testLinks(): Links {
    return Links(
        missionPatch = null,
        missionPatchSmall = null,
        redditCampaign = null,
        redditLaunch = null,
        redditRecovery = null,
        redditMedia = null,
        pressKit = null,
        article = null,
        wikipedia = null,
        video = null,
        youtubeKey = null,
        flickrImages = listOf()
    )
}

private fun testRocketSummary(): RocketSummary {
    return RocketSummary(
        rocketId = "Test ID",
        name = "Test Name",
        type = "Test Type",
        firstStage = testFirstStage(),
        secondState = testSecondStage(),
        fairing = testFairing()
    )
}

private fun testFirstStage(): FirstStageSummary {
    return FirstStageSummary(cores = listOf())
}

private fun testSecondStage(): SecondStageSummary {
    return SecondStageSummary(block = null, payloads = listOf())
}

private fun testFairing(): Fairing {
    return Fairing(reused = false, recovered = false, recoveryAttempt = false, ship = "Test Ship")
}

private fun testTelemetry(): Telemetry {
    return Telemetry(flightClub = "Test Flight Club")
}

private fun testTimeline(): Timeline {
    return Timeline(
        webcastLiftoff = null,
        goForPropLoading = null,
        rp1Loading = null,
        stage1LoxLoading = null,
        stage2LoxLoading = null,
        engineChill = null,
        prelaunchChecks = null,
        propellantPressurization = null,
        goForLaunch = null,
        ignition = null,
        liftoff = null,
        maxQ = null,
        meco = null,
        stageSeparation = null,
        secondStateIgnition = null,
        fairingDeploy = null,
        firstStageEntryBurn = null,
        seco1 = null,
        fistStageLanding = null,
        secondStageRestart = null,
        seco2 = null,
        payloadDeploy = null
    )
}