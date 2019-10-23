package com.haroldadmin.moonshot.rockets

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.base.koin
import com.haroldadmin.moonshotRepository.rocket.GetAllRocketsUseCase
import com.haroldadmin.vector.VectorViewModelFactory
import com.haroldadmin.vector.ViewModelOwner
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

    companion object : VectorViewModelFactory<RocketsViewModel, RocketsState> {
        override fun create(
            initialState: RocketsState,
            owner: ViewModelOwner,
            handle: SavedStateHandle
        ): RocketsViewModel? = with(owner.koin()) {
            RocketsViewModel(initialState, get())
        }
    }
}