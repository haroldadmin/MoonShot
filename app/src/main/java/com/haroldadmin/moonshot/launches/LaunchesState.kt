package com.haroldadmin.moonshot.launches

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launch.Launch

data class LaunchesState(
    val launches: Resource<List<Launch>> = Resource.Unitialized
): MoonShotState