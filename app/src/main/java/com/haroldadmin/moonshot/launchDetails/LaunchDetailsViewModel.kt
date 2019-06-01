package com.haroldadmin.moonshot.launchDetails

import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class LaunchDetailsViewModel(
    initialState: LaunchDetailsState,
    private val launchesRepository: LaunchesRepository
) : MoonShotViewModel<LaunchDetailsState>(initialState) {

    init {
        getLaunchDetails()
    }

    private fun getLaunchDetails() {
        withState { state ->
            viewModelScope.launch {
                executeAsResource({ copy(launch = it) }) { launchesRepository.getLaunch(state.flightNumber) }
            }
        }
    }


    companion object : MvRxViewModelFactory<LaunchDetailsViewModel, LaunchDetailsState> {
        override fun create(viewModelContext: ViewModelContext, state: LaunchDetailsState): LaunchDetailsViewModel? {
            return LaunchDetailsViewModel(
                state,
                viewModelContext.activity.get<LaunchesRepository>()
            )
        }
    }
}