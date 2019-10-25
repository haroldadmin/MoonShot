package com.haroldadmin.moonshotRepository.mappers

import com.haroldadmin.spacex_api_wrapper.common.Length
import com.haroldadmin.spacex_api_wrapper.common.Mass
import com.haroldadmin.spacex_api_wrapper.common.Location
import com.haroldadmin.spacex_api_wrapper.common.Thrust
import com.haroldadmin.spacex_api_wrapper.common.Volume
import com.haroldadmin.moonshot.models.common.Length as DbLength
import com.haroldadmin.moonshot.models.common.Location as DbLocation
import com.haroldadmin.moonshot.models.common.Mass as DbMass
import com.haroldadmin.moonshot.models.common.Thrust as DbThrust
import com.haroldadmin.moonshot.models.common.Volume as DbVolume

internal fun Thrust.toDbThrust(): DbThrust {
    return DbThrust(
        kN = this.kN,
        lbf = this.lbf
    )
}

internal fun Length.toDbLength(): DbLength {
    return DbLength(
        metres = this.meters,
        feet = this.feet
    )
}

internal fun Mass.toDbMass(): DbMass {
    return DbMass(
        kg = this.kg,
        lb = this.lb
    )
}

internal fun Location.toDbLocation(): DbLocation {
    return DbLocation(
        name = this.name,
        region = this.region,
        latitude = this.latitude,
        longitude = this.longitude
    )
}

internal fun Volume.toDbVolume(): DbVolume {
    return DbVolume(
        cubicMeters = this.cubicMeters,
        cubicFeet = this.cubicFeet
    )
}