package com.haroldadmin.moonshot.launches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.LaunchTypes
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.pairOf
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get
import java.util.Calendar

class LaunchesViewModel(
    initState: LaunchesState,
    private val launchesRepository: LaunchesRepository
) : MoonShotViewModel<LaunchesState>(initState) {

    init {
        viewModelScope.launch {
            when (initState.type) {
                LaunchTypes.NORMAL -> getAllLaunches()
                LaunchTypes.LAUNCHPAD -> getLaunchesForLaunchPad(initState.siteId)
            }
        }
    }

    suspend fun getAllLaunches() = withState { state ->
        val (minTimeStamp, maxTimeStamp) = getTimeStampRange(state)

        launchesRepository
            .flowAllMinimalLaunches(
                limit = 15,
                maxTimestamp = maxTimeStamp,
                minTimeStamp = minTimeStamp
            )
            .collect { setState { copy(launches = it) } }
    }

    suspend fun getLaunchesForLaunchPad(siteId: String?) = withState { state ->
        val (minTimeStamp, maxTimeStamp) = getTimeStampRange(state)

        if (siteId == null) {
            throw IllegalArgumentException("Site ID is needed to fetch launches for the launchpad")
        }

        launchesRepository
            .flowLaunchesForLaunchPad(siteId, maxTimeStamp, minTimeStamp)
            .collect { resource ->
                setState {
                    val site = (resource as? Resource.Success)?.data?.firstOrNull()?.siteName
                    copy(launches = resource, siteName = site)
                }
            }
    }

    fun setFilter(filter: LaunchesFilter) = viewModelScope.launch {
        withState { state ->
            setState { copy(filter = filter, launches = Resource.Loading) }
            when (state.type) {
                LaunchTypes.NORMAL -> getAllLaunches()
                LaunchTypes.LAUNCHPAD -> getLaunchesForLaunchPad(state.siteId)
            }
        }
    }

    private fun getTimeStampRange(state: LaunchesState): Pair<Long, Long> {
        var maxTimeStamp = Long.MAX_VALUE
        var minTimeStamp = Long.MIN_VALUE

        when (state.filter) {
            LaunchesFilter.PAST -> {
                maxTimeStamp = Calendar.getInstance().timeInMillis
            }
            LaunchesFilter.UPCOMING -> {
                minTimeStamp = Calendar.getInstance().timeInMillis
            }
            LaunchesFilter.ALL -> Unit
        }

        return pairOf(minTimeStamp, maxTimeStamp)
    }
}

@Suppress("UNCHECKED_CAST")
class LaunchesViewModelFactory(private val initialState: LaunchesState) : ViewModelProvider.Factory,
    KoinComponent {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LaunchesViewModel(initialState, get()) as T
    }
}