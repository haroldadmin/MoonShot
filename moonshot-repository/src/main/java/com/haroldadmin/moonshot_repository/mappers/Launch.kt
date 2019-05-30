package com.haroldadmin.moonshot_repository.mappers

import com.haroldadmin.spacex_api_wrapper.common.OrbitParams
import com.haroldadmin.spacex_api_wrapper.launches.*
import com.haroldadmin.moonshot.models.launch.Launch as DbLaunch
import com.haroldadmin.moonshot.models.launch.LaunchSite as DbLaunchSite
import com.haroldadmin.moonshot.models.launch.Links as DbLinks
import com.haroldadmin.moonshot.models.launch.Telemetry as DbTelemetry
import com.haroldadmin.moonshot.models.launch.Timeline as DbTimeline
import com.haroldadmin.moonshot.models.launch.rocket.Fairings as DbFairings
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary as DbRocketSummary
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.FirstStageSummary as DbFirstStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.CoreSummary as DbCoreSummary
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.SecondStageSummary as DbSecondStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.payload.Payload as DbPayload
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.payload.OrbitParams as DbOrbitParams

internal fun Launch.toDbLaunch(): DbLaunch {
    return DbLaunch(
        flightNumber,
        missionName,
        missionId,
        launchYear,
        launchDate,
        isTentative,
        tentativeMaxPrecision,
        tbd,
        launchWindow,
        ships,
        launchSuccess,
        details,
        upcoming,
        staticFireDate,
        telemetry.toDbTelemetry(),
        launchSite.toDbLaunchSite(),
        links.toDbLinks(),
        timeline?.toDbTimeline()
    )
}

internal fun Telemetry.toDbTelemetry(): DbTelemetry {
    return DbTelemetry(
        flightClub
    )
}

internal fun LaunchSite.toDbLaunchSite(): DbLaunchSite {
    return DbLaunchSite(
        id,
        name,
        nameLong
    )
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

internal fun Fairing.toDbFairings(): DbFairings {
    return DbFairings(
        reused = this.reused,
        recovered = this.recovered,
        recoveryAttempt = this.recoveryAttempt,
        ship = this.ship
    )
}

internal fun CoreSummary.toDbCoreSummary(firstStageSummaryId: Long): DbCoreSummary {
    return DbCoreSummary(
        serial = this.serial!!,
        reused = this.reused,
        block = this.block,
        firstStageSummaryId = firstStageSummaryId,
        flight = this.flight,
        gridfins = this.gridfins,
        landingIntent = this.landingIntent,
        landingType = this.landingType,
        landingVehicle = this.landingVehicle,
        landSuccess = this.landSuccess,
        legs = this.legs
    )
}

internal fun FirstStageSummary.toDbFirstStageSummary(rocketId: String): DbFirstStageSummary {
    return DbFirstStageSummary(
        rocketId = rocketId
    )
}

internal fun OrbitParams.toDbOrbitParams(): DbOrbitParams {
    return DbOrbitParams(
        referenceSystem = this.referenceSystem,
        longitude = this.longitude,
        apoapsis = this.apoapsisKm,
        argOfPericenter = this.argOfPericenter,
        eccentricity = this.eccentricity,
        epoch = this.epoch,
        inclinationDeg = this.inclinationDeg,
        lifespanYears = this.lifespanYears,
        meanAnomaly = this.meanAnomaly,
        meanMotion = this.meanMotion,
        periapsisKm = this.periapsisKm,
        periodMin = this.periodMin,
        raan = this.raan,
        regime = this.regime,
        semiMajorAxisKm = this.semiMajorAxisKm
    )
}

internal fun Payload.toDbPayload(secondStageSummaryId: Long): DbPayload {
    return DbPayload(
        id = this.id,
        customers = this.customers,
        manufacturer = this.manufacturer,
        nationality = this.nationality,
        noradId = this.noradId,
        orbit = this.orbit,
        orbitParams = this.orbitParams.toDbOrbitParams(),
        payloadMassKg = this.payloadMassKg,
        payloadMassLbs = this.payloadMassLbs,
        payloadType = this.payloadType,
        reused = this.reused,
        secondStageSummaryId = secondStageSummaryId
    )
}

internal fun SecondStageSummary.toDbSecondStageSummary(rocketId: String): DbSecondStageSummary {
    return DbSecondStageSummary(
        rocketId = rocketId,
        block = this.block
    )
}

internal fun RocketSummary.toDbRocketSummary(flightNumber: Int): DbRocketSummary {
    return DbRocketSummary(
        rocketId = this.rocketId,
        launchFlightNumber = flightNumber,
        rocketName = this.name,
        rocketType = this.type,
        fairings = this.fairing?.toDbFairings()
    )
}