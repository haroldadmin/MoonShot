package com.haroldadmin.moonshot.features.rocketDetails

import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.Rocket

data class RocketDetailsState(
    val rocketId: String,
    val rocket: Resource<Rocket> = Resource.Uninitialized,
    val launches: Resource<List<Launch>> = Resource.Uninitialized
) : MoonShotState