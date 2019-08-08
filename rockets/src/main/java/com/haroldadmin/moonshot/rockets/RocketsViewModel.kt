package com.haroldadmin.moonshot.rockets

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshotRepository.rocket.GetAllRocketsUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RocketsViewModel(
    initState: RocketsState,
    private val allRocketsUseCase: GetAllRocketsUseCase
) : MoonShotViewModel<RocketsState>(initState) {

    init {
        viewModelScope.launch { getAllRockets() }
    }

    private suspend fun getAllRockets() {
        allRocketsUseCase
            .getAllRockets()
            .collect { rocketsRes ->
                setState { copy(rockets = rocketsRes) }
            }
    }
}