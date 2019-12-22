package com.haroldadmin.moonshot.launchDetails

import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.LinkPreview
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.LaunchPictures

data class LaunchDetailsState(
    val flightNumber: Int,
    val launch: Resource<Launch> = Resource.Uninitialized,
    val launchPictures: Resource<LaunchPictures> = Resource.Uninitialized,
    val linkPreviews: List<LinkPreview> = emptyList()
) : MoonShotState
