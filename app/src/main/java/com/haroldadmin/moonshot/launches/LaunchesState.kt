package com.haroldadmin.moonshot.launches

import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launch.Launch

data class LaunchesState(
    val launches: Resource<List<Launch>> = Resource.Uninitialized,
    val nextLaunch: Resource<Launch> = Resource.Uninitialized
) : MoonShotState