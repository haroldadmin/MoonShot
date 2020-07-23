package com.haroldadmin.moonshot.features.search

import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.LaunchPad
import com.haroldadmin.moonshot.models.Rocket

data class SearchState(
    val query: String = "",
    val launches: Resource<List<Launch>> = Resource.Uninitialized,
    val rockets: Resource<List<Rocket>> = Resource.Uninitialized,
    val launchPads: Resource<List<LaunchPad>> = Resource.Uninitialized
) : MoonShotState {

    val isLoading: Boolean
        get() {
            return launches is Resource.Loading &&
                    rockets is Resource.Loading &&
                    launchPads is Resource.Loading
        }

    val isUninitialized: Boolean
        get() {
            return query.isBlank() || (launches is Resource.Uninitialized &&
                    rockets is Resource.Uninitialized &&
                    launchPads is Resource.Uninitialized)
        }
}