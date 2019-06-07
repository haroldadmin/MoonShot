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
        getLaunchDetails(initState.flightNumber)
    }

    private fun getLaunchDetails(flightNumber: Int) = viewModelScope.launch {
        executeAsResource({ copy(launch = it) }) { launchesRepository.getLaunch(flightNumber) }
    }

}