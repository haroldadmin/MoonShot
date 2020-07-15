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
import com.haroldadmin.moonshot.services.spacex.v4.Payload as APIPayload
import com.haroldadmin.moonshot.services.spacex.v4.RoadsterInfo as APIRoadster
import com.haroldadmin.moonshot.services.spacex.v4.Rocket as APIRocket
import com.haroldadmin.moonshot.services.spacex.v4.Ship as APIShip

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

fun APILaunch.Core.toDBModel(launchID: String): LaunchCore {
    return LaunchCore(
        id = this.id,
        flight = this.flight,
        gridfins = this.gridfins,
        legs = this.legs,
        reused = this.reused,
        landingAttempt = this.landingAttempt,
        landingSuccess = this.landingSuccess,
        landingType = this.landingType,
        landPadID = this.landpadID,
        launchID = launchID
    )
}

fun APIPayload.toDBModel(): Payload {
    return Payload(
        id = this.id,
        name = this.name,
        type = this.type,
        launchID = this.launchID,
        customers = this.customers.map { it },
        noradIDs = this.noradIDs.map { it },
        nationalities = this.nationalities.map { it },
        manufacturers = this.manufacturers.map { it },
        massKg = this.massKg,
        massLbs = this.massLbs,
        orbit = this.orbit,
        referenceSystem = this.referenceSystem,
        regime = this.regime,
        longitude = this.longitude,
        semiMajorAxisKm = this.semiMajorAxisKm,
        eccentricity = this.eccentricity,
        periapsisKm = this.periapsisKm,
        apoapsisKm = this.apoapsisKm,
        inclinationDeg = this.inclinationDeg,
        periodMin = this.periodMin,
        lifespanYears = this.lifespanYears,
        epoch = this.epoch,
        meanMotion = this.meanMotion,
        raan = this.raan,
        argOfPericentre = this.argOfPericentre,
        meanAnomaly = this.meanAnomaly,
        reused = this.reused,
        dragon_capsuleID = this.dragon?.capsuleID,
        dragon_massReturnedKg = this.dragon?.massReturnedKg,
        dragon_massReturnedLbs = this.dragon?.massReturnedLbs,
        dragon_flightTimeSec = this.dragon?.flightTimeSec,
        dragon_manifest = this.dragon?.manifest,
        dragon_landLanding = this.dragon?.landLanding,
        dragon_waterLanding = this.dragon?.waterLanding
    )
}

fun APIRoadster.toDBModel(): RoadsterInfo {
    return RoadsterInfo(
        id = this.id,
        name = this.name,
        launchDateUTC = this.launchDateUTC,
        launchDateUnix = this.launchDateUnix,
        launchMassLbs = this.launchMassLbs,
        launchMassKg = this.launchMassKg,
        noradID = this.noradID,
        epochJD = this.epochJd,
        orbitType = this.orbitType,
        apoapsisAu = this.apoapsisAu,
        periapsisAu = this.periapsisAu,
        semiMajorAxisAu = this.semiMajorAxisAu,
        eccentricity = this.eccentricity,
        inclination = this.inclination,
        longitude = this.longitude,
        periapsisArg = this.periapsisArg,
        periodDays = this.periodDays,
        speedKph = this.speedKph,
        speedMph = this.speedMph,
        earthDistanceKm = this.earthDistanceKm,
        earthDistanceMi = this.earthDistanceMiles,
        marsDistanceKm = this.marsDistanceKm,
        marsDistanceMi = this.marsDistanceMiles,
        flickrImages = this.flickrImages.map { it },
        wikipedia = this.wikipedia,
        video = this.video,
        details = this.details
    )
}

fun APIRocket.toDBModel(): Rocket {
    return Rocket(
        id = this.id,
        name = this.name,
        type = this.type,
        active = this.active,
        stages = this.stages,
        boosters = this.boosters,
        costPerLaunch = this.costPerLaunch,
        successRatePercentage = this.successRatePercentage,
        firstFlight = this.firstFlight,
        country = this.country,
        company = this.company,
        height_feet = this.height?.feet,
        height_metres = this.height?.metres,
        diameter_feet = this.diameter?.feet,
        diameter_metres = this.diameter?.metres,
        mass_kg = this.mass?.kg,
        mass_lb = this.mass?.lb,
        firstStage_reusable = this.firstStage?.reusable,
        firstStage_engines = this.firstStage?.engines,
        firstStage_fuelAmountTons = this.firstStage?.fuelAmountTons,
        firstStage_burnTimeSec = this.firstStage?.burnTimeSec,
        firstStage_thrustSeaLevel_kN = this.firstStage?.thrustSeaLevel?.kN,
        firstStage_thrustSeaLevel_lbf = this.firstStage?.thrustSeaLevel?.lbf,
        firstStage_thrustVacuum_kN = this.firstStage?.thrustVacuum?.kN,
        firstStage_thrustVacuum_lbf = this.firstStage?.thrustVacuum?.lbf,
        secondStage_reusable = this.secondStage?.reusable,
        secondStage_engines = this.secondStage?.engines,
        secondStage_fuelAmountTons = this.secondStage?.fuelAmountTons,
        secondStage_thrust_kN = this.secondStage?.thrust?.kN,
        secondStage_thrust_lbf = this.secondStage?.thrust?.lbf,
        secondStage_burnTimeSec = this.secondStage?.burnTimeSec,
        secondStage_payloads_optionOne = this.secondStage?.payloads?.optionOne,
        secondStage_payloads_compositeFairing_height_feet = this.secondStage?.payloads?.compositeFairing?.height?.feet,
        secondStage_payloads_compositeFairing_height_metres = this.secondStage?.payloads?.compositeFairing?.height?.metres,
        engines_number = this.engines?.number,
        engines_type = this.engines?.type,
        engines_version = this.engines?.version,
        engines_layout = this.engines?.layout,
        engines_isp_seaLevel = this.engines?.isp?.seaLevel,
        engines_isp_vacuum = this.engines?.isp?.vacuum,
        engines_engineLossMax = this.engines?.engineLossMax,
        engines_propellantOne = this.engines?.propellantOne,
        engines_propellantTwo = this.engines?.propellantTwo,
        engines_thrustSeaLevel_kN = this.engines?.thrustSeaLevel?.kN,
        engines_thrustSeaLevel_lbf = this.engines?.thrustSeaLevel?.lbf,
        engines_thrustVacuum_kN = this.engines?.thrustVacuum?.kN,
        engines_thrustVacuum_lbf = this.engines?.thrustVacuum?.lbf,
        engines_thrustToWeight = this.engines?.thrustToWeight,
        landingLegs_material = this.landingLegs?.material,
        landingLegs_number = this.landingLegs?.number,
        flickrImages = this.flickrImages.map { it },
        wikipedia = this.wikipedia,
        description = this.description
    )
}

fun APIRocket.PayloadWeight.toDBModel(rocketID: String): RocketPayloadWeight {
    return RocketPayloadWeight(
        id = this.id,
        name = this.name,
        kg = this.kg,
        lb = this.lb,
        rocketID = rocketID
    )
}

fun APIShip.toDBModel(): Ship {
    return Ship(
        id = this.id,
        name = this.name,
        model = this.model,
        type = this.type,
        roles = this.roles.map { it },
        isActive = this.isActive,
        imo = this.imo,
        mmsi = this.mmsi,
        abs = this.abs,
        clazz = this.clazz,
        massKg = this.massKg,
        massLbs = this.massLbs,
        yearBuilt = this.yearBuilt,
        homePort = this.homePort,
        status = this.status,
        speedKn = this.speedKn,
        courseDeg = this.courseDeg,
        latitude = this.latitude,
        longitude = this.longitude,
        lastAisUpdate = this.lastAisUpdate,
        link = this.link,
        image = this.image,
        launchIDs = this.launchIDs.map { it }
    )
}
