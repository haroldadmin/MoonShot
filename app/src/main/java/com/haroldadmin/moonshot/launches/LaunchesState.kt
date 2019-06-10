package com.haroldadmin.moonshot.launches

import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.LaunchMinimal

data class LaunchesState(
    val launches: Resource<List<LaunchMinimal>> = Resource.Uninitialized
) : MoonShotState