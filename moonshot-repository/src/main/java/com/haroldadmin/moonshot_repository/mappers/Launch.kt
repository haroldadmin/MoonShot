package com.haroldadmin.moonshot_repository.mappers

import com.haroldadmin.spacex_api_wrapper.launches.Launch
import com.haroldadmin.spacex_api_wrapper.launches.LaunchSite
import com.haroldadmin.spacex_api_wrapper.launches.Links
import com.haroldadmin.spacex_api_wrapper.launches.Telemetry
import com.haroldadmin.spacex_api_wrapper.launches.Timeline
import com.haroldadmin.moonshot.models.launch.Launch as DbLaunch
import com.haroldadmin.moonshot.models.launch.Telemetry as DbTelemetry
import com.haroldadmin.moonshot.models.launch.LaunchSite as DbLaunchSite
import com.haroldadmin.moonshot.models.launch.Links as DbLinks
import com.haroldadmin.moonshot.models.launch.Timeline as DbTimeline

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