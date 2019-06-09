package com.haroldadmin.moonshot.nextLaunch

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import kotlinx.coroutines.launch
import java.util.Calendar

class NextLaunchViewModel(
    initState: NextLaunchState,
    private val launchesRepository: LaunchesRepository
) : MoonShotViewModel<NextLaunchState>(initState) {

    init {
        viewModelScope.launch {
            getNextLaunch(Calendar.getInstance().timeInMillis)
        }
    }

    suspend fun getNextLaunch(currentTime: Long) {
        executeAsResource(
            reducer = { copy(nextLaunch = it) },
            after = this::startCountdown
        ) {
            launchesRepository.getNextLaunch(currentTime)
        }
    }

    private suspend fun startCountdown() = withState { state ->
        if (state.nextLaunch is Resource.Success) {
        }
    }
}