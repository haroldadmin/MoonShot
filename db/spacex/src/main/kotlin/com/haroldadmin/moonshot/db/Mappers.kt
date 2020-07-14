package com.haroldadmin.moonshot.db

import com.haroldadmin.moonshot.services.spacex.v4.Capsule as APICapsule
import com.haroldadmin.moonshot.services.spacex.v4.CompanyInfo as APICompanyInfo
import com.haroldadmin.moonshot.services.spacex.v4.Core as APICore
import com.haroldadmin.moonshot.services.spacex.v4.Crew as APICrew
import com.haroldadmin.moonshot.services.spacex.v4.Dragon as APIDragon
import com.haroldadmin.moonshot.services.spacex.v4.Dragon.Thruster as APIThruster
import com.haroldadmin.moonshot.services.spacex.v4.LandingPad as APILandingPad
import com.haroldadmin.moonshot.services.spacex.v4.Launch as APILaunch
import com.haroldadmin.moonshot.services.spacex.v4.LaunchPad as APILaunchPad

fun APICapsule.toDBModel(): Capsule {
    return Capsule(
        id = this.id,
        launchIDs = this.launchIDs.map { it },
        dragon = this.dragon,
        landLandings = this.landLandings,
        lastUpdate = this.lastUpdate,
        reuseCount = this.reuseCount,
        serial = this.serial,
        status = this.status,
        waterLandings = this.waterLandings
    )
}

fun APICompanyInfo.toDBModel(): CompanyInfo {
    return CompanyInfo(
        id = this.id,
        ceo = this.ceo,
        coo = this.coo,
        cto = this.cto,
        ctoPropulsion = this.ctoPropulsion,
        employees = this.employees,
        founded = this.founded,
        founder = this.founder,
        launchSites = this.launchSites,
        name = this.name,
        testSites = this.testSites,
        valuation = this.valuation,
        vehicles = this.vehicles,
        headquarters_address = this.headquarters?.address,
        headquarters_city = this.headquarters?.city,
        headquarters_state = this.headquarters?.state,
        links_website = this.links?.website,
        links_twitter = this.links?.twitter,
        links_flickr = this.links?.flickr,
        links_elonTwitter = this.links?.elonTwitter
    )
}

fun APICore.toDBModel(): Core {
    return Core(
        id = this.id,
        serial = this.serial,
        block = this.block,
        status = this.status,
        reuseCount = this.reuseCount,
        rtlsAttempts = this.rtlsAttempts,
        rtlsLandings = this.rtlsLandings,
        asdsAttempts = this.asdsAttempts,
        asdsLandings = this.asdsLandings,
        lastUpdate = this.lastUpdate,
        launchIDs = this.launchIDs.map { it }
    )
}

fun APICrew.toDBModel(): Crew {
    return Crew(
        id = this.id,
        name = this.name,
        status = this.status,
        agency = this.agency,
        image = this.image,
        wikipedia = this.wikipedia,
        launchIDs = this.launchIDs.map { it }
    )
}

fun APIDragon.toDBModel(): Dragon {
    return Dragon(
        id = this.id,
        name = this.name,
        type = this.type,
        active = this.active,
        crewCapacity = this.crewCapacity,
        sidewallAngleDeg = this.sidewallAngleDegrees,
        orbitDurationYears = this.orbitDurationYears,
        dryMassKg = this.dryMassKg,
        dryMassLb = this.dryMassLb,
        firstFlight = this.firstFlight,
        heatShield_material = this.heatShield?.material,
        heatShield_sizeMetres = this.heatShield?.sizeMetre,
        heatShield_devPartner = this.heatShield?.devPartner,
        heatShield_tempDegrees = this.heatShield?.tempDegree,
        launchPayloadMass_kg = this.launchPayloadMass?.kg,
        launchPayloadMass_lb = this.launchPayloadMass?.lb,
        launchPayloadVol_cubicFeet = this.launchPayloadVolume?.cubicFeet,
        launchPayloadVol_cubicMetres = this.launchPayloadVolume?.cubicMetres,
        returnPayloadMass_kg = this.returnPayloadMass?.kg,
        returnPayloadMass_lb = this.returnPayloadMass?.lb,
        returnPayloadVol_cubicMetres = this.returnPayloadVolume?.cubicMetres,
        returnPayloadVol_cubicFeet = this.returnPayloadVolume?.cubicFeet,
        pressurizedCapsule_payloadVolume_cubicMetres = this.pressurizedCapsule?.payloadVolume?.cubicMetres,
        pressurizedCapsule_payloadVolume_cubicFeet = this.pressurizedCapsule?.payloadVolume?.cubicFeet,
        trunk_trunkVolume_cubicMetres = this.trunk?.trunkVolume?.cubicMetres,
        trunk_trunkVolume_cubicFeet = this.trunk?.trunkVolume?.cubicFeet,
        trunk_cargo_solarArray = this.trunk?.cargo?.solarArray,
        trunk_cargo_unpressurizedCargo = this.trunk?.cargo?.unpressurizedCargo,
        heightWithTrunk_metres = this.heightWithTrunk?.metres,
        heightWithTrunk_feet = this.heightWithTrunk?.feet,
        diameter_metres = this.diameter?.metres,
        diameter_feet = this.diameter?.feet,
        wikipedia = this.wikipedia,
        description = this.description,
        flickrImages = this.flickrImages.map { it }
    )
}

fun APIThruster.toDBModel(dragonID: String): Thruster {
    return Thruster(
        type = this.type,
        amount = this.amount,
        pods = this.pods,
        fuelOne = this.fuelOne,
        fuelTwo = this.fuelTwo,
        isp = this.isp,
        thrust_kN = this.thrust?.kN,
        thrust_lbf = this.thrust?.lbf,
        dragonID = dragonID
    )
}

fun APILandingPad.toDBModel(): LandingPad {
    return LandingPad(
        name = this.name,
        fullName = this.fullName,
        status = this.status,
        type = this.type,
        locality = this.locality,
        region = this.region,
        latitude = this.latitude,
        longitude = this.longitude,
        landingAttempts = this.landingAttempts,
        landingSuccesses = this.landingSuccesses,
        wikipedia = this.wikipedia,
        details = this.details,
        launchIDs = this.launchIDs.map { it },
        id = this.id
    )
}

fun APILaunch.toDBModel(): Launch {
    return Launch(
        id = this.id,
        flightNumber = this.flightNumber,
        name = this.name,
        launchDateUTC = this.launchDateUTC,
        launchDateLocal = this.launchDateLocal,
        launchDateUnix = this.launchDateUnix,
        datePrecision = this.datePrecision,
        staticFireDateUTC = this.staticFireDateUTC,
        staticFireDateUnix = this.staticFireDateUnix,
        tbd = this.tbd,
        net = this.net,
        window = this.window,
        rocketID = this.rocketID,
        success = this.success,
        failures = this.failures.map { it },
        upcoming = this.upcoming,
        details = this.details,
        fairings_recovered = this.fairings?.recovered,
        fairings_reused = this.fairings?.reused,
        fairings_recoveryAttempt = this.fairings?.recoveryAttempted,
        fairings_shipIDs = this.fairings?.shipIDs?.map { it },
        crewIDs = this.crewIDs.map { it },
        shipIDs = this.shipIDs.map { it },
        capsuleIDs = this.capsuleIDs.map { it },
        payloadIDs = this.payloadIDs.map { it },
        launchPadID = this.launchpadID,
        links_patch_small = this.links?.patch?.small,
        links_patch_large = this.links?.patch?.large,
        links_reddit_campaign = this.links?.reddit?.campaign,
        links_reddit_launch = this.links?.reddit?.launch,
        links_reddit_media = this.links?.reddit?.media,
        links_flickr_small = this.links?.flickr?.small,
        links_flickr_original = this.links?.flickr?.original,
        links_presskit = this.links?.presskit,
        links_webcast = this.links?.webcast,
        links_youtubeID = this.links?.youtubeID,
        links_article = this.links?.article,
        links_wikipedia = this.links?.wikipedia,
        autoUpdate = this.autoUpdate
    )
}

fun APILaunchPad.toDBModel(): LaunchPad {
    return LaunchPad(
        id = this.id,
        name = this.name,
        fullName = this.fullName,
        status = this.status,
        locality = this.locality,
        region = this.region,
        timezone = this.timezone,
        latitude = this.latitude,
        longitude = this.longitude,
        launchAttempts = this.launchAttempts,
        launchSuccesses = this.launchSuccesses,
        rocketIDs = this.rocketIDs.map { it },
        launchIDs = this.launchIDs.map { it }
    )
}
