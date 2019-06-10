package com.haroldadmin.moonshot.launchDetails

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.launch

class LaunchDetailsViewModel(
    initState: LaunchDetailsState,
    private val launchesRepository: LaunchesRepository
) : MoonShotViewModel<LaunchDetailsState>(initState) {

    init {
        viewModelScope.launch {
            getLaunchDetails(initState.flightNumber)
            getLaunchStats(initState.flightNumber)
            getLaunchPictures(initState.flightNumber)
        }
    }


    suspend fun getLaunchDetails(flightNumber: Int) =
        launchesRepository.flowLaunch(flightNumber)
            .collect { setState { copy(launch = it) } }

    suspend fun getLaunchStats(flightNumber: Int) =
        launchesRepository.flowLaunchStats(flightNumber)
            .collect { setState { copy(launchStats = it) } }

    suspend fun getLaunchPictures(flightNumber: Int) =
        launchesRepository.flowLaunchPictures(flightNumber)
            .collect { setState { copy(launchPictures = it) } }
}