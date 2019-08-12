package com.haroldadmin.moonshot.rockets

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshotRepository.rocket.GetAllRocketsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RocketsViewModel(
    initState: RocketsState,
    stateStoreContext: CoroutineContext = Dispatchers.Default + Job(),
    private val allRocketsUseCase: GetAllRocketsUseCase
) : MoonShotViewModel<RocketsState>(initState, stateStoreContext) {

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