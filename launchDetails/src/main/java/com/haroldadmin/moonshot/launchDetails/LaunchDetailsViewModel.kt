package com.haroldadmin.moonshot.launchDetails

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshotRepository.launch.GetLaunchDetailsUseCase
import com.haroldadmin.moonshotRepository.launch.GetLaunchPicturesUseCase
import com.haroldadmin.moonshotRepository.launch.GetLaunchStatsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class LaunchDetailsViewModel(
    initState: LaunchDetailsState,
    private val launchDetailsUseCase: GetLaunchDetailsUseCase,
    private val launchStatsUseCase: GetLaunchStatsUseCase,
    private val launchPicturesUseCase: GetLaunchPicturesUseCase
) : MoonShotViewModel<LaunchDetailsState>(initState) {

    init {
        viewModelScope.launch {
            getLaunchDetails(initState.flightNumber)
            getLaunchStats(initState.flightNumber)
            getLaunchPictures(initState.flightNumber)
        }
    }

    suspend fun getLaunchDetails(flightNumber: Int) {
        launchDetailsUseCase
            .getLaunchDetails(flightNumber)
            .collect { launchRes ->
                setState {
                    copy(launch = launchRes)
                }
            }
    }

    suspend fun getLaunchStats(flightNumber: Int) {
        launchStatsUseCase
            .getLaunchStats(flightNumber)
            .collect { statsRes ->
                setState {
                    copy(launchStats = statsRes)
                }
            }
    }

    suspend fun getLaunchPictures(flightNumber: Int) {
        launchPicturesUseCase
            .getLaunchPictures(flightNumber)
            .collect { picturesRes ->
                setState {
                    copy(launchPictures = picturesRes)
                }
            }
    }
}