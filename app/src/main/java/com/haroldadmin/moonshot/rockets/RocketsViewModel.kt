package com.haroldadmin.moonshot.rockets

import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshotRepository.rocket.RocketsRepository
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class RocketsViewModel(
    initialState: RocketsState,
    private val repository: RocketsRepository
) : MoonShotViewModel<RocketsState>(initialState) {

    init {
        viewModelScope.launch {
            getAllRockets()
        }
    }

    suspend fun getAllRockets() {
        executeAsResource({ copy(rockets = it) }) {
            repository.getAllRockets()
        }
    }

    companion object : MvRxViewModelFactory<RocketsViewModel, RocketsState> {
        override fun create(viewModelContext: ViewModelContext, state: RocketsState): RocketsViewModel? {
            return RocketsViewModel(state, viewModelContext.activity.get<RocketsRepository>())
        }
    }
}