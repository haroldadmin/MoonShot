package com.haroldadmin.moonshot.launchPad

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshotRepository.launchPad.GetLaunchPadUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LaunchPadViewModel(
    initState: LaunchPadState,
    private val launchPadUseCase: GetLaunchPadUseCase
) : MoonShotViewModel<LaunchPadState>(initState) {

    init {
        viewModelScope.launch {
            getLaunchPad(initState.siteId)
        }
    }

    suspend fun getLaunchPad(siteId: String) {
        launchPadUseCase
            .getLaunchPad(siteId)
            .collect { launchPadRes ->
                setState { copy(launchPad = launchPadRes) }
            }
    }
}