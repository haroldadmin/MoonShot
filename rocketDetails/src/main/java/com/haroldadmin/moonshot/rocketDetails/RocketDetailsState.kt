package com.haroldadmin.moonshot.rocketDetails

import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.rocket.RocketMinimal

data class RocketDetailsState(
    val rocketId: String,
    val rocket: Resource<RocketMinimal> = Resource.Uninitialized,
    val launches: Resource<List<LaunchMinimal>> = Resource.Uninitialized
) : MoonShotState