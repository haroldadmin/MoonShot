package com.haroldadmin.moonshot.launchDetails

import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary

data class LaunchDetailsState(
    val flightNumber: Int,
    val launch: Resource<Launch> = Resource.Uninitialized,
    val rocketSummary: Resource<RocketSummary> = Resource.Uninitialized
) : MoonShotState