package com.haroldadmin.moonshot.rockets

import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.rocket.RocketMinimal

data class RocketsState(
    val rockets: Resource<List<RocketMinimal>> = Resource.Uninitialized
) : MoonShotState