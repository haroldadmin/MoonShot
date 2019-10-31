package com.haroldadmin.moonshotRepository.mappers

import com.haroldadmin.moonshot.models.Mission as DbMission
import com.haroldadmin.spacex_api_wrapper.mission.Mission

fun Mission.toDbMission(): DbMission {
    return DbMission(
        name = this.name,
        id = this.id,
        manufacturers = this.manufacturers,
        payloadIds = this.payloadIds,
        wikipedia = this.wikipedia,
        website = this.website,
        twitter = this.twitter,
        description = this.description
    )
}