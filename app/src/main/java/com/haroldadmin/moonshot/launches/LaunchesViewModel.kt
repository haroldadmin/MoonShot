package com.haroldadmin.moonshot.launches

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import kotlinx.coroutines.launch
import java.util.Date

class LaunchesViewModel(
    initState: LaunchesState,
    private val launchesRepository: LaunchesRepository
) : MoonShotViewModel<LaunchesState>(initState) {

    init {
        viewModelScope.launch {
            getAllLaunches()
        }
    }

    suspend fun getAllLaunches() {
        executeAsResource({ copy(launches = it) }) { launchesRepository.getAllLaunches() }
    }

    suspend fun getUpcomingLaunches() {
        executeAsResource({ copy(launches = it) }) {
            launchesRepository.getUpcomingLaunches(Date().time)
        }
    }

    suspend fun getPastLaunches() {
        executeAsResource({ copy(launches = it) }) {
            launchesRepository.getPastLaunches(Date().time)
        }
    }

//    private suspend fun getNextLaunch() {
//        executeAsResource({ copy(nextLaunch = it) }) {
//            launchesRepository.getNextLaunch(Date().time)
//        }
//    }
}