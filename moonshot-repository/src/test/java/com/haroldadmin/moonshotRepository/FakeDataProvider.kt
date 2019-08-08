package com.haroldadmin.moonshotRepository

import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.launch.toLaunchMinimal
import com.haroldadmin.spacex_api_wrapper.common.Length
import com.haroldadmin.spacex_api_wrapper.common.Mass
import com.haroldadmin.spacex_api_wrapper.common.Thrust
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
import com.haroldadmin.spacex_api_wrapper.rocket.CompositeFairing
import com.haroldadmin.spacex_api_wrapper.rocket.Engines
import com.haroldadmin.spacex_api_wrapper.rocket.FirstStage
import com.haroldadmin.spacex_api_wrapper.rocket.LandingLegs
import com.haroldadmin.spacex_api_wrapper.rocket.Payloads
import com.haroldadmin.spacex_api_wrapper.rocket.Rocket
import com.haroldadmin.spacex_api_wrapper.rocket.SecondStage
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

    fun getApiRockets(number: Int): List<Rocket> {
        var count = 0
        return generateSequence {
            Rocket(
                rocketId = "Test ID",
                firstStage = testFirstStage(),
                engines = testEngines(),
                wikipedia = "",
                active = false,
                boosters = 0,
                company = "",
                costPerLaunch = 0,
                country = "",
                description = "",
                diameter = testLength(),
                firstFlight = "",
                height = testLength(),
                id = ++count,
                landingLegs = testLandingLegs(),
                mass = testMass(),
                payloadWeights = listOf(),
                rocketName = "Test Rocket",
                rocketType = "Test Type",
                secondStage = testSecondStage(),
                stages = 0,
                successRate = 0.0
            )
        }.take(number).toList()
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
        firstStage = testFirstStageSummary(),
        secondState = testSecondStageSummary(),
        fairing = testFairing()
    )
}

private fun testFirstStageSummary(): FirstStageSummary {
    return FirstStageSummary(cores = listOf())
}

private fun testSecondStageSummary(): SecondStageSummary {
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

private fun testFirstStage(): FirstStage {
    return FirstStage(
        reusable = false,
        engines = 0,
        fuelAmountTons = 0.0,
        thrustSeaLevel = testThrust(),
        thrustVacuum = testThrust(),
        burnTimeSecs = 0.0
    )
}

private fun testThrust(): Thrust {
    return Thrust(
        kN = 0.0,
        lbf = 0.0
    )
}

private fun testEngines(): Engines {
    return Engines(
        number = 0,
        thrustVacuum = testThrust(),
        thrustSeaLevel = testThrust(),
        type = "Test Type",
        engineLossMax = 0,
        layout = "Test Layout",
        propellant1 = "Test Propellant",
        propellant2 = "Test Propellant",
        version = "Test Version",
        thrustToWeightRatio = 0.0
    )
}

private fun testLength(): Length {
    return Length(
        meters = 0.0,
        feet = 0.0
    )
}

private fun testMass(): Mass {
    return Mass(
        kg = 0.0,
        lb = 0.0
    )
}

private fun testLandingLegs(): LandingLegs {
    return LandingLegs(
        number = 1,
        material = "Test Material"
    )
}

private fun testSecondStage(): SecondStage {
    return SecondStage(
        burnTimeSecs = 0.0,
        engines = 0,
        fuelAmountTons = 0.0,
        payloads = testPayloads(),
        thrust = testThrust()
    )
}

private fun testPayloads(): Payloads {
    return Payloads(
        option1 = null,
        option2 = null,
        compositeFairing = testCompositeFairing()
    )
}

private fun testCompositeFairing(): CompositeFairing {
    return CompositeFairing(
        height = testLength(),
        diameter = testLength()
    )
}