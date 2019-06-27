package com.haroldadmin.moonshot.rockets

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshotRepository.rocket.RocketsRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RocketsViewModel(
    initState: RocketsState,
    private val repository: RocketsRepository
) : MoonShotViewModel<RocketsState>(initState) {

    init {
        viewModelScope.launch { getAllRockets() }
    }

    suspend fun getAllRockets() {
        repository.flowAllRocketsMinimal()
            .collect { setState { copy(rockets = it) } }
    }
}