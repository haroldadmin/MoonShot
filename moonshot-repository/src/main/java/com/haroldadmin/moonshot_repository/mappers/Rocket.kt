package com.haroldadmin.moonshot_repository.mappers

import com.haroldadmin.spacex_api_wrapper.rocket.FirstStage
import com.haroldadmin.spacex_api_wrapper.rocket.Rocket
import com.haroldadmin.moonshot.models.rocket.Rocket as DbRocket
import com.haroldadmin.moonshot.models.rocket.first_stage.FirstStage as DbFirstStage

internal fun Rocket.toDbRocket(): DbRocket {
    return DbRocket(
        rocketId = this.rockedId,
        rocketName = this.rocketName,
        rocketType = this.rocketType,
        id = this.id,
        active = this.active,
        stages = this.stages,
        boosters = this.boosters,
        costPerLaunch = this.costPerLaunch,
        successRatePercentage = this.successRate,
        firstFlight = this.firstFlight,
        country = this.country,
        company = this.company,
        height = this.height.toDbLength(),
        diameter = this.diameter.toDbLength(),
        mass = this.mass.toDbMass(),
        firstStage = this.firstStage.toDbFirstStage(),

    )
}

internal fun FirstStage.toDbFirstStage(): DbFirstStage {
    return DbFirstStage(
        reusable = this.reusable,
        engines = this.engines,
        fuelAmountTons = this.fuelAmountTons,
        burnTimeSec = this.burnTimeSecs,
        thrustSeaLevel = this.thrustSeaLevel.toDbThrust(),
        thrustVacuum = this.thrustVacuum.toDbThrust()
    )
}

internal fun