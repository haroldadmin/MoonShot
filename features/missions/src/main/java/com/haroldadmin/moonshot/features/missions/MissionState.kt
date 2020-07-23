package com.haroldadmin.moonshot.features.missions

import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.LinkPreview
import com.haroldadmin.moonshot.models.Mission

data class MissionState(
    val missionId: String? = null,
    val mission: Resource<Mission> = Resource.Uninitialized,
    val linkPreviews: List<LinkPreview> = emptyList()
) : MoonShotState
