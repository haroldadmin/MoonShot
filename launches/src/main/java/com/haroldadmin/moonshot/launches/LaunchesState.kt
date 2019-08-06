package com.haroldadmin.moonshot.launches

import com.haroldadmin.moonshot.LaunchTypes
import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launch.LaunchMinimal

data class LaunchesState(
    val type: LaunchTypes = LaunchTypes.NORMAL,
    val siteId: String? = null,
    val siteName: String? = null,
    val filter: LaunchesFilter = LaunchesFilter.PAST,
    val launches: Resource<List<LaunchMinimal>> = Resource.Uninitialized
) : MoonShotState

enum class LaunchesFilter {
    PAST, UPCOMING, ALL
}