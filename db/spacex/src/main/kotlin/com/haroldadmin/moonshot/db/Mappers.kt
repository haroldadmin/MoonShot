package com.haroldadmin.moonshot.db

import com.haroldadmin.moonshot.services.spacex.v4.Capsule as APICapsule
import com.haroldadmin.moonshot.services.spacex.v4.CompanyInfo as APICompanyInfo
import com.haroldadmin.moonshot.services.spacex.v4.Core as APICore
import com.haroldadmin.moonshot.services.spacex.v4.Crew as APICrew
import com.haroldadmin.moonshot.services.spacex.v4.Dragon as APIDragon
import com.haroldadmin.moonshot.services.spacex.v4.Dragon.Thruster as APIThruster
import com.haroldadmin.moonshot.services.spacex.v4.LandingPad as APILandingPad

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
        name = "LZ-2",
        fullName = "Landing Zone 2",
        status = "active",
        type = "RTLS",
        locality = "Cape Canaveral",
        region = "Florida",
        latitude = 28.485833,
        longitude = -80.544444,
        landingAttempts = 3,
        landingSuccesses = 3,
        wikipedia = "https://en.wikipedia.org/wiki/Landing_Zones_1_and_2",
        details = "SpaceX's first east coast landing pad is Landing Zone 1, where the historic first Falcon 9 landing occurred in December 2015. LC-13 was originally used as a launch pad for early Atlas missiles and rockets from Lockheed Martin. LC-1 was later expanded to include Landing Zone 2 for side booster RTLS Falcon Heavy missions, and it was first used in February 2018 for that purpose.",
        launchIDs = listOf(
            "5eb87d13ffd86e000604b360",
            "5eb87d2dffd86e000604b376",
            "5eb87d35ffd86e000604b37a"
        ),
        id = "5e9e3032383ecb90a834e7c8"
    )
}