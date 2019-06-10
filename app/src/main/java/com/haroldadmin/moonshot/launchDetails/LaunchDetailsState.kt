package com.haroldadmin.moonshot.launchDetails

import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.launch.LaunchPictures
import com.haroldadmin.moonshot.models.launch.LaunchStats

data class LaunchDetailsState(
    val flightNumber: Int,
    val launch: Resource<LaunchMinimal> = Resource.Uninitialized,
    val launchStats: Resource<LaunchStats> = Resource.Uninitialized,
    val launchPictures: Resource<LaunchPictures> = Resource.Uninitialized
) : MoonShotState

