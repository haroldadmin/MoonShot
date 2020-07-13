package com.haroldadmin.moonshot.db

import com.haroldadmin.moonshot.services.spacex.v4.Capsule as APICapsule
import com.haroldadmin.moonshot.services.spacex.v4.CompanyInfo as APICompanyInfo
import com.haroldadmin.moonshot.services.spacex.v4.Core as APICore

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