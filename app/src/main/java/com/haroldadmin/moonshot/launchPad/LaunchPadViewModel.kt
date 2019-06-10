package com.haroldadmin.moonshot.launchPad

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshotRepository.launchPad.LaunchPadRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LaunchPadViewModel(
    initState: LaunchPadState,
    private val launchPadRepository: LaunchPadRepository
) : MoonShotViewModel<LaunchPadState>(initState) {

    init {
        viewModelScope.launch {
            getLaunchPad(initState.siteId)
        }
    }

    suspend fun getLaunchPad(siteId: String) {
        launchPadRepository.flowLaunchPad(siteId)
            .collect { setState { copy(launchPad = it) } }
    }
}