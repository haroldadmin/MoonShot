package com.haroldadmin.moonshot.launches

import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import java.util.Date

class LaunchesViewModel(
    initialState: LaunchesState,
    private val launchesRepository: LaunchesRepository
) : MoonShotViewModel<LaunchesState>(initialState) {

    init {
        viewModelScope.launch {
            getAllLaunches()
        }
    }

    private var offset = 0
    private var limit = 15

    fun getLaunches() {

    }

    private suspend fun getAllLaunches() {
        executeAsResource({ copy(launches = it) }) { launchesRepository.getAllLaunches() }
    }

    private suspend fun getUpcomingLaunches() {
        executeAsResource({ copy(launches = it) }) {
            launchesRepository.getUpcomingLaunches(Date().time)
        }
    }

    private suspend fun getPastLaunches() {
        executeAsResource({ copy(launches = it) }) {
            launchesRepository.getPastLaunches(Date().time)
        }
    }
//
//    private suspend fun getNextLaunch() {
//        executeAsResource({ copy(nextLaunch = it) }) {
//            launchesRepository.getNextLaunch(Date().time)
//        }
//    }

    companion object : MvRxViewModelFactory<LaunchesViewModel, LaunchesState> {
        override fun create(viewModelContext: ViewModelContext, state: LaunchesState): LaunchesViewModel? {
            val repository = viewModelContext.activity.get<LaunchesRepository>()
            return LaunchesViewModel(state, repository)
        }
    }
}