package com.haroldadmin.moonshotRepository.mappers

import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPad
import com.haroldadmin.moonshot.models.launchpad.LaunchPad as DbLaunchPad

internal fun LaunchPad.toDbLaunchPad(): DbLaunchPad {
    return DbLaunchPad(
        id = this.id,
        status = this.status,
        vehiclesLanded = this.vehiclesLaunched,
        attemptedLaunches = this.attemptedLaunches,
        successfulLaunches = this.successfulLaunches,
        wikipedia = this.wikipedia,
        details = this.details,
        siteId = this.siteId,
        siteNameLong = this.siteNameLong,
        location = this.location.toDbLocation()
    )
}