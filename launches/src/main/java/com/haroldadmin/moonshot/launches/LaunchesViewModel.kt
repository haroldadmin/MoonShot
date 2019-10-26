package com.haroldadmin.moonshot.launches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.LaunchTypes
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshotRepository.launch.GetLaunchesForLaunchpadUseCase
import com.haroldadmin.moonshotRepository.launch.GetLaunchesUseCase
import com.haroldadmin.moonshotRepository.launch.LaunchType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get

@ExperimentalCoroutinesApi
class LaunchesViewModel(
    initState: LaunchesState,
    private val launchesUseCase: GetLaunchesUseCase,
    private val launchesForLaunchpadUseCase: GetLaunchesForLaunchpadUseCase
) : MoonShotViewModel<LaunchesState>(initState) {

    init {
        viewModelScope.launch {
            when (initState.type) {
                LaunchTypes.NORMAL -> getAllLaunches()
                LaunchTypes.LAUNCHPAD -> getLaunchesForLaunchPad(initState.siteId)
            }
        }
    }

    private suspend fun getAllLaunches() = withState { state ->
        launchesUseCase
            .getLaunches(state.filter, limit = 15)
            .collect { launchesResource ->
                setState {
                    copy(launches = launchesResource)
                }
            }
    }

    private suspend fun getLaunchesForLaunchPad(siteId: String?) = withState { state ->
        requireNotNull(siteId) { "Site ID is needed to fetch launches for the launchpad" }

        launchesForLaunchpadUseCase
            .getLaunchesForLaunchpad(siteId, state.filter, limit = Int.MAX_VALUE)
            .collect { launchesResource ->
                val siteName = (launchesResource as? Resource.Success)?.data?.firstOrNull()?.launchSite?.siteName
                setState { copy(launches = launchesResource, siteName = siteName) }
            }
    }

    fun setFilter(filter: LaunchType) = viewModelScope.launch {
        withState { state ->
            if (state.filter != filter) {
                setState { copy(filter = filter, launches = Resource.Loading) }
                when (state.type) {
                    LaunchTypes.NORMAL -> getAllLaunches()
                    LaunchTypes.LAUNCHPAD -> getLaunchesForLaunchPad(state.siteId)
                }
            }
        }
    }
}

@ExperimentalCoroutinesApi
@Suppress("UNCHECKED_CAST")
class LaunchesViewModelFactory(private val initialState: LaunchesState) : ViewModelProvider.Factory,
    KoinComponent {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LaunchesViewModel(initialState, get(), get()) as T
    }
}