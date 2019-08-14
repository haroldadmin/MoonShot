package com.haroldadmin.moonshot.search

import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.models.launchpad.LaunchPad
import com.haroldadmin.moonshot.models.rocket.RocketMinimal

data class SearchState(
    val query: String = "",
    val launches: Resource<List<LaunchMinimal>> = Resource.Uninitialized,
    val rockets: Resource<List<RocketMinimal>> = Resource.Uninitialized,
    val launchPads: Resource<List<LaunchPad>> = Resource.Uninitialized
) : MoonShotState