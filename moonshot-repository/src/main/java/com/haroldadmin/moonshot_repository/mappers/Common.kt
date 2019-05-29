package com.haroldadmin.moonshot_repository.mappers

import com.haroldadmin.spacex_api_wrapper.common.Length
import com.haroldadmin.spacex_api_wrapper.common.Mass
import com.haroldadmin.spacex_api_wrapper.common.Thrust
import com.haroldadmin.moonshot.models.common.Thrust as DbThrust
import com.haroldadmin.moonshot.models.common.Length as DbLength
import com.haroldadmin.moonshot.models.common.Mass as DbMass

internal fun Thrust.toDbThrust(): DbThrust {
    return DbThrust(
        kN = this.kN,
        lbf = this.lbf
    )
}

internal fun Length.toDbLength(): DbLength {
    return DbLength(
        meters = this.meters,
        feet = this.feet
    )
}

internal fun Mass.toDbMass(): DbMass {
    return DbMass(
        kg = this.kg,
        lb = this.lb
    )
}