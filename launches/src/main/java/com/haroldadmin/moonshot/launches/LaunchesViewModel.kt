package com.haroldadmin.moonshot.launches

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.LaunchTypes
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Calendar

class LaunchesViewModel(
    initState: LaunchesState,
    private val launchesRepository: LaunchesRepository
) : MoonShotViewModel<LaunchesState>(initState) {

    init {
        viewModelScope.launch {
            when (initState.type) {
                LaunchTypes.RECENT -> getAllLaunches()
                LaunchTypes.UPCOMING -> setState { copy(launches = Resource.Error(null, "Unsupported")) }
                LaunchTypes.LAUNCHPAD -> getLaunchesForLaunchPad(initState.siteId)
            }
        }
    }

    suspend fun getAllLaunches() {
        val currentTime = Calendar.getInstance().timeInMillis
        launchesRepository
            .flowAllMinimalLaunches(limit = 15, maxTimestamp = currentTime)
            .collect { setState { copy(launches = it) } }
    }

    suspend fun getLaunchesForLaunchPad(siteId: String?) {
        if (siteId == null) {
            setState {
                copy(
                    launches = Resource.Error(
                        null,
                        IllegalArgumentException("Site ID is needed to get launches for it")
                    )
                )
            }
            return
        }
        val currentTime = Calendar.getInstance().timeInMillis
        launchesRepository
            .flowLaunchesForLaunchPad(siteId, currentTime)
            .collect { resource ->
                setState {
                    val site = (resource as? Resource.Success)?.data?.firstOrNull()?.siteName
                    copy(launches = resource, siteName = site)
                }
            }
    }
}