package com.haroldadmin.moonshot.launches

import com.haroldadmin.moonshot.LaunchTypes
import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshotRepository.launch.LaunchType

data class LaunchesState(
    val type: LaunchTypes = LaunchTypes.NORMAL,
    val siteId: String? = null,
    val siteName: String? = null,
    val filter: LaunchType = LaunchType.Recent,
    val launches: Resource<List<Launch>> = Resource.Uninitialized
) : MoonShotState