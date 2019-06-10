package com.haroldadmin.moonshot.launches

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Calendar
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
        val currentTime = Calendar.getInstance().timeInMillis
        launchesRepository
            .flowAllLaunches(limit = 20, maxTimestamp = currentTime)
            .collect { setState { copy(launches = it) } }
    }

    suspend fun getUpcomingLaunches(currentTime: Long, limit: Int) {
        launchesRepository.flowUpcomingLaunches(currentTime, limit)
            .collect { launches -> setState { copy(launches = launches) } }
    }

    suspend fun getPastLaunches(currentTime: Long, limit: Int) {
        launchesRepository.flowPastLaunches(currentTime, limit)
            .collect { launches -> setState { copy(launches = launches) } }
    }
}