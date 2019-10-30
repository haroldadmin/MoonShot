package com.haroldadmin.moonshot.rockets

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshotRepository.rocket.GetAllRocketsUseCase
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class RocketsViewModel @AssistedInject constructor(
    @Assisted initState: RocketsState,
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

    @AssistedInject.Factory
    interface Factory {
        fun create(initState: RocketsState): RocketsViewModel
    }
}