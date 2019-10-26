package com.haroldadmin.moonshot.rocketDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.base.koin
import com.haroldadmin.moonshot.base.safeArgs
import com.haroldadmin.moonshotRepository.rocket.GetLaunchesForRocketUseCase
import com.haroldadmin.moonshotRepository.rocket.GetRocketDetailsUseCase
import com.haroldadmin.vector.VectorViewModelFactory
import com.haroldadmin.vector.ViewModelOwner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class RocketDetailsViewModel(
    initState: RocketDetailsState,
    private val rocketDetailsUseCase: GetRocketDetailsUseCase,
    private val launchesForRocketUseCase: GetLaunchesForRocketUseCase
) : MoonShotViewModel<RocketDetailsState>(initState) {

    init {
        viewModelScope.launch {
            getRocketDetails(initState.rocketId)
            getLaunches(initState.rocketId)
        }
    }

    suspend fun getRocketDetails(rocketId: String) {
        rocketDetailsUseCase.getRocketDetails(rocketId)
            .collect { rocketDetailsRes ->
                setState { copy(rocket = rocketDetailsRes) }
            }
    }

    suspend fun getLaunches(rocketId: String, limit: Int = 10) {
        launchesForRocketUseCase
            .getLaunchesForRocket(rocketId, limit)
            .collect { launchesRes ->
                setState { copy(launches = launchesRes) }
            }
    }

    companion object : VectorViewModelFactory<RocketDetailsViewModel, RocketDetailsState> {
        override fun create(
            initialState: RocketDetailsState,
            owner: ViewModelOwner,
            handle: SavedStateHandle
        ): RocketDetailsViewModel? = with(owner.koin()) {
            RocketDetailsViewModel(initialState, get(), get())
        }

        override fun initialState(
            handle: SavedStateHandle,
            owner: ViewModelOwner
        ): RocketDetailsState? {
            val safeArgs = owner.safeArgs<RocketDetailsFragmentArgs>()
            return RocketDetailsState(safeArgs.rocketId)
        }
    }
}