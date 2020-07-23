package com.haroldadmin.moonshot.features.launchPad

import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.LaunchPad

data class LaunchPadState(
    val siteId: String,
    val launchPad: Resource<LaunchPad> = Resource.Uninitialized
) : MoonShotState