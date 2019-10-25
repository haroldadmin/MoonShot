package com.haroldadmin.moonshotRepository.mappers

import com.haroldadmin.moonshot.models.toDatePrecision
import com.haroldadmin.spacex_api_wrapper.common.OrbitParams
import com.haroldadmin.spacex_api_wrapper.launches.Launch
import com.haroldadmin.spacex_api_wrapper.launches.Telemetry
import com.haroldadmin.spacex_api_wrapper.launches.LaunchSite
import com.haroldadmin.spacex_api_wrapper.launches.Links
import com.haroldadmin.spacex_api_wrapper.launches.Timeline
import com.haroldadmin.spacex_api_wrapper.launches.Fairing
import com.haroldadmin.spacex_api_wrapper.launches.CoreSummary
import com.haroldadmin.spacex_api_wrapper.launches.FirstStageSummary
import com.haroldadmin.spacex_api_wrapper.launches.Payload
import com.haroldadmin.spacex_api_wrapper.launches.SecondStageSummary
import com.haroldadmin.spacex_api_wrapper.launches.RocketSummary
import com.haroldadmin.moonshot.models.launch.Launch as DbLaunch
import com.haroldadmin.moonshot.models.launch.LaunchSite as DbLaunchSite
import com.haroldadmin.moonshot.models.launch.Links as DbLinks
import com.haroldadmin.moonshot.models.launch.Telemetry as DbTelemetry
import com.haroldadmin.moonshot.models.launch.Timeline as DbTimeline
import com.haroldadmin.moonshot.models.launch.Rocket as DbLaunchRocket
import com.haroldadmin.moonshot.models.launch.Fairings as DbFairings
//import com.haroldadmin.moonshot.models.launch.rocket.firstStage.FirstStageSummary as DbFirstStageSummary
//import com.haroldadmin.moonshot.models.launch.rocket.firstStage.CoreSummary as DbCoreSummary
//import com.haroldadmin.moonshot.models.launch.rocket.secondStage.SecondStageSummary as DbSecondStageSummary
//import com.haroldadmin.moonshot.models.launch.rocket.secondStage.payload.Payload as DbPayload
//import com.haroldadmin.moonshot.models.launch.rocket.secondStage.payload.OrbitParams as DbOrbitParams

internal fun Launch.toDbLaunch(): DbLaunch {
    return DbLaunch(
        flightNumber = flightNumber,
        missionName = missionName,
        missionId = missionId,
        launchYear = launchYear,
        launchDateUtc = launchDate,
        isTentative = isTentative,
        tentativeMaxPrecision = tentativeMaxPrecision.toDatePrecision(),
        tbd = tbd,
        launchWindow = launchWindow?.toLong(),
        ships = ships,
        launchSuccess = launchSuccess,
        details = details,
        isUpcoming = upcoming,
        staticFireDateUtc = staticFireDate,
        telemetry = telemetry.toDbTelemetry(),
        launchSite = launchSite.toDbLaunchSite(),
        links = links.toDbLinks(),
        timeline = timeline?.toDbTimeline(),
        rocket = rocket.toDbLaunchRocket()
    )
}

internal fun Telemetry.toDbTelemetry(): DbTelemetry {
    return DbTelemetry(flightClub)
}

internal fun LaunchSite.toDbLaunchSite(): DbLaunchSite {
    return DbLaunchSite(id, name, nameLong)
}

internal fun Links.toDbLinks(): DbLinks {
    return DbLinks(
        missionPatch,
        missionPatchSmall,
        redditCampaign,
        redditLaunch,
        redditRecovery,
        redditMedia,
        pressKit,
        article,
        wikipedia,
        video,
        youtubeKey,
        flickrImages
    )
}

internal fun Timeline.toDbTimeline(): DbTimeline {
    return DbTimeline(
        webcastLiftoff,
        goForPropLoading,
        rp1Loading,
        stage1LoxLoading,
        stage2LoxLoading,
        engineChill,
        prelaunchChecks,
        propellantPressurization,
        goForLaunch,
        ignition,
        liftoff,
        maxQ,
        meco,
        stageSeparation,
        secondStateIgnition,
        fairingDeploy,
        firstStageEntryBurn,
        seco1,
        fistStageLanding,
        secondStageRestart,
        seco2,
        payloadDeploy

    )
}

internal fun RocketSummary.toDbLaunchRocket(): DbLaunchRocket {
    return DbLaunchRocket(
        rocketId = this.rocketId,
        rocketName = this.name,
        rocketType = this.type,
        fairings = this.fairing?.toDbFairings()
    )
}

internal fun Fairing.toDbFairings(): DbFairings {
    return DbFairings(
        reused = this.reused,
        recovered = this.recovered,
        recoveryAttempt = this.recoveryAttempt,
        ship = this.ship
    )
}
//
//internal fun CoreSummary.toDbCoreSummary(flightNumber: Int): DbCoreSummary {
//    return DbCoreSummary(
//        flightNumber = flightNumber,
//        serial = this.serial ?: "Unknown",
//        reused = this.reused,
//        block = this.block,
//        flight = this.flight,
//        gridfins = this.gridfins,
//        landingIntent = this.landingIntent,
//        landingType = this.landingType,
//        landingVehicle = this.landingVehicle,
//        landSuccess = this.landSuccess,
//        legs = this.legs
//    )
//}
//
//internal fun FirstStageSummary.toDbFirstStageSummary(flightNumber: Int): DbFirstStageSummary {
//    return DbFirstStageSummary(
//        flightNumber = flightNumber
//    )
//}
//
//internal fun OrbitParams.toDbOrbitParams(): DbOrbitParams {
//    return DbOrbitParams(
//        referenceSystem = this.referenceSystem,
//        longitude = this.longitude,
//        apoapsis = this.apoapsisKm,
//        argOfPericenter = this.argOfPericenter,
//        eccentricity = this.eccentricity,
//        epoch = this.epoch,
//        inclinationDeg = this.inclinationDeg,
//        lifespanYears = this.lifespanYears,
//        meanAnomaly = this.meanAnomaly,
//        meanMotion = this.meanMotion,
//        periapsisKm = this.periapsisKm,
//        periodMin = this.periodMin,
//        raan = this.raan,
//        regime = this.regime,
//        semiMajorAxisKm = this.semiMajorAxisKm
//    )
//}
//
//internal fun Payload.toDbPayload(flightNumber: Int): DbPayload {
//    return DbPayload(
//        flightNumber = flightNumber,
//        id = this.id,
//        customers = this.customers,
//        manufacturer = this.manufacturer,
//        nationality = this.nationality,
//        noradId = this.noradId,
//        orbit = this.orbit,
//        orbitParams = this.orbitParams.toDbOrbitParams(),
//        payloadMassKg = this.payloadMassKg,
//        payloadMassLbs = this.payloadMassLbs,
//        payloadType = this.payloadType,
//        reused = this.reused
//    )
//}
//
//internal fun SecondStageSummary.toDbSecondStageSummary(flightNumber: Int): DbSecondStageSummary {
//    return DbSecondStageSummary(
//        flightNumber = flightNumber,
//        block = this.block
//    )
//}