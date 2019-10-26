package com.haroldadmin.moonshot.nextLaunch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.base.koin
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import com.haroldadmin.vector.VectorViewModelFactory
import com.haroldadmin.vector.ViewModelOwner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class NextLaunchViewModel(
    initState: NextLaunchState,
    private val nextLaunchUseCase: GetNextLaunchUseCase
) : MoonShotViewModel<NextLaunchState>(initState) {

    init {
        viewModelScope.launch {
            getNextLaunch()
        }
    }

    suspend fun getNextLaunch() {
        nextLaunchUseCase
            .getNextLaunch()
            .collect { nextLaunchRes ->
                setState { copy(nextLaunch = nextLaunchRes) }
            }
    }

    companion object : VectorViewModelFactory<NextLaunchViewModel, NextLaunchState> {
        override fun create(
            initialState: NextLaunchState,
            owner: ViewModelOwner,
            handle: SavedStateHandle
        ): NextLaunchViewModel? = with(owner.koin()) {
            NextLaunchViewModel(initialState, get())
        }
    }
}
