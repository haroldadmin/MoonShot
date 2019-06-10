package com.haroldadmin.moonshot.nextLaunch

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import kotlinx.coroutines.flow.collect
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

    suspend fun getNextLaunch(currentTime: Long) =
        launchesRepository.flowNextLaunch(currentTime)
            .collect { setState { copy(nextLaunch = it) } }
}