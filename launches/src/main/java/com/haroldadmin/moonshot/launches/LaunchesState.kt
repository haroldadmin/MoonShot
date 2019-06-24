package com.haroldadmin.moonshot.launches

import com.haroldadmin.moonshot.LaunchTypes
import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launch.LaunchMinimal

data class LaunchesState(
    val type: LaunchTypes = LaunchTypes.RECENT,
    val siteId: String? = null,
    val siteName: String? = null,
    val launches: Resource<List<LaunchMinimal>> = Resource.Uninitialized
) : MoonShotState