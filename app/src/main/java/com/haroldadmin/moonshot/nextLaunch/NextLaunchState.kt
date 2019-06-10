package com.haroldadmin.moonshot.nextLaunch

import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.LaunchMinimal

data class NextLaunchState(
    val nextLaunch: Resource<LaunchMinimal> = Resource.Uninitialized
) : MoonShotState