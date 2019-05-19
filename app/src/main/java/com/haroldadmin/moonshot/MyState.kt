package com.haroldadmin.moonshot

import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.launch.Launch

data class MyState (val launches: Resource<List<Launch>>): MoonShotState