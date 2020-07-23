package com.haroldadmin.moonshot.features.launches

import com.haroldadmin.moonshot.navigation.LaunchTypes
import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshotRepository.launch.LaunchType

data class LaunchesState(
    val type: LaunchTypes = LaunchTypes.NORMAL,
    val siteId: String? = null,
    val siteName: String? = null,
    val filter: LaunchType = LaunchType.Recent,
    val launchesRes: Resource<List<Launch>> = Resource.Uninitialized,
    val launches: List<Launch> = launchesRes() ?: emptyList(),
    val hasMoreToFetch: Boolean = true
) : MoonShotState {
    val offset: Int = launches.size
}