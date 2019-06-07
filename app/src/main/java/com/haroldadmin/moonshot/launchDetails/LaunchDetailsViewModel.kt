package com.haroldadmin.moonshot.launchDetails

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import kotlinx.coroutines.launch

class LaunchDetailsViewModel(
    initState: LaunchDetailsState,
    private val launchesRepository: LaunchesRepository
) : MoonShotViewModel<LaunchDetailsState>(initState) {

    init {
        viewModelScope.launch {
            getLaunchDetails(initState.flightNumber)
            getRocketSummary(initState.flightNumber)
        }
    }

    suspend fun getLaunchDetails(flightNumber: Int) {
        executeAsResource({ copy(launch = it) }) { launchesRepository.getLaunch(flightNumber) }
    }

    suspend fun getRocketSummary(flightNumber: Int) {
        executeAsResource({ copy(rocketSummary = it) }) { launchesRepository.getRocketSummary(flightNumber) }
    }

}