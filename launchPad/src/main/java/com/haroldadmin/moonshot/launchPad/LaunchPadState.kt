package com.haroldadmin.moonshot.launchPad

import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launchpad.LaunchPad as LaunchPadModel

data class LaunchPadState(
    val siteId: String,
    val launchPad: Resource<LaunchPadModel> = Resource.Uninitialized
) : MoonShotState