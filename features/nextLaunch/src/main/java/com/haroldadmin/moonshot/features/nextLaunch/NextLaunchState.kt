package com.haroldadmin.moonshot.features.nextLaunch

import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launch.Launch

data class NextLaunchState(
    val nextLaunchResource: Resource<Launch> = Resource.Uninitialized
) : MoonShotState