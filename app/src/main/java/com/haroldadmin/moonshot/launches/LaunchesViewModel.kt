package com.haroldadmin.moonshot.launches

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
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
            getAllLaunches()
        }
    }

    suspend fun getAllLaunches() {
        val currentTime = Calendar.getInstance().timeInMillis
        launchesRepository
            .flowAllMinimalLaunches(limit = 15, maxTimestamp = currentTime)
            .collect { setState { copy(launches = it) } }
    }
}