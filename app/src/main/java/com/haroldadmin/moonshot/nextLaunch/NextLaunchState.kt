package com.haroldadmin.moonshot.nextLaunch

import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launch.Launch

data class NextLaunchState(
    val nextLaunch: Resource<Launch> = Resource.Uninitialized
) : MoonShotState