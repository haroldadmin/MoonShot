package com.haroldadmin.moonshot.launches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.LaunchTypes
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.pairOf
import com.haroldadmin.moonshotRepository.launch.GetLaunchesForLaunchpadUseCase
import com.haroldadmin.moonshotRepository.launch.GetLaunchesUseCase
import com.haroldadmin.moonshotRepository.launch.LaunchesFilter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get
import java.util.Calendar

class LaunchesViewModel(
    initState: LaunchesState,
    private val launchesUseCase: GetLaunchesUseCase,
    private val launchesForLaunchpadUseCase: GetLaunchesForLaunchpadUseCase
) : MoonShotViewModel<LaunchesState>(initState) {

    private val currentTime: Long
        get() = Calendar.getInstance().timeInMillis

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
            .getLaunches(state.filter, currentTime, 15)
            .collect { launchesResource ->
                setState {
                    copy(launches = launchesResource)
                }
            }
    }

    private suspend fun getLaunchesForLaunchPad(siteId: String?) = withState { state ->
        val (from, to) = getTimeStampRange(state)

        if (siteId == null) {
            throw IllegalArgumentException("Site ID is needed to fetch launches for the launchpad")
        }

        launchesForLaunchpadUseCase
            .getLaunchesForLaunchpad(siteId, from, to, currentTime, Int.MAX_VALUE)
            .collect { launchesResource ->
                val siteName = (launchesResource as? Resource.Success)?.data?.firstOrNull()?.siteName
                setState { copy(launches = launchesResource, siteName = siteName) }
            }
    }

    fun setFilter(filter: LaunchesFilter) = viewModelScope.launch {
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

    private fun getTimeStampRange(state: LaunchesState): Pair<Long, Long> {
        var maxTimeStamp = Long.MAX_VALUE
        var minTimeStamp = Long.MIN_VALUE

        when (state.filter) {
            LaunchesFilter.PAST -> maxTimeStamp = currentTime
            LaunchesFilter.UPCOMING -> minTimeStamp = currentTime
            LaunchesFilter.ALL -> Unit
        }

        return pairOf(minTimeStamp, maxTimeStamp)
    }
}

@Suppress("UNCHECKED_CAST")
class LaunchesViewModelFactory(private val initialState: LaunchesState) : ViewModelProvider.Factory,
    KoinComponent {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LaunchesViewModel(initialState, get(), get()) as T
    }
}