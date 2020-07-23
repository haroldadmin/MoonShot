package com.haroldadmin.moonshot.features.launches

import com.haroldadmin.moonshot.navigation.LaunchTypes
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.append
import com.haroldadmin.moonshot.core.appendUnique
import com.haroldadmin.moonshot.core.hasAtLeastSize
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshot.utils.launchAfterDelay
import com.haroldadmin.moonshotRepository.launch.GetLaunchesForLaunchpadUseCase
import com.haroldadmin.moonshotRepository.launch.GetLaunchesUseCase
import com.haroldadmin.moonshotRepository.launch.LaunchType
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

private const val fetchLimit: Int = 15

@ExperimentalCoroutinesApi
class LaunchesViewModel @AssistedInject constructor(
    @Assisted initState: LaunchesState,
    private val launchesUseCase: GetLaunchesUseCase,
    private val launchesForLaunchpadUseCase: GetLaunchesForLaunchpadUseCase
) : MoonShotViewModel<LaunchesState>(initState) {

    init {
        when (initState.type) {
            LaunchTypes.NORMAL -> getAllLaunches()
            LaunchTypes.LAUNCHPAD -> getLaunchesForLaunchPad(initState.siteId)
        }
    }

    private fun getAllLaunches() = withState { state ->
        launchesUseCase
            .getLaunches(state.filter, limit = fetchLimit, offset = state.offset)
            .collect { launchesResource ->
                setState {
                    copy(
                        launchesRes = launchesResource,
                        launches = launches.appendUnique(launchesResource()),
                        hasMoreToFetch = launchesResource().hasAtLeastSize(fetchLimit)
                    )
                }
            }
    }

    private fun getLaunchesForLaunchPad(siteId: String?) = withState { state ->
        requireNotNull(siteId) { "Site ID is needed to fetch launches for the launchpad" }
        launchesForLaunchpadUseCase
            .getLaunchesForLaunchpad(siteId, state.filter, limit = fetchLimit, offset = state.offset)
            .collect { launchesResource ->
                setState {
                    copy(
                        launchesRes = launchesResource,
                        launches = launches.append(launchesResource()),
                        siteName = launchesResource()?.firstOrNull()?.launchSite?.siteName,
                        hasMoreToFetch = launchesResource().hasAtLeastSize(fetchLimit)
                    )
                }
            }
    }

    fun setFilter(filter: LaunchType) = withState { state ->
        if (state.filter != filter) {
            setState {
                copy(filter = filter, launchesRes = Resource.Loading, launches = emptyList())
            }
            when (state.type) {
                LaunchTypes.NORMAL -> getAllLaunches()
                LaunchTypes.LAUNCHPAD -> getLaunchesForLaunchPad(state.siteId)
            }
        }
    }

    fun loadMore() = withState { state ->
        when (state.type) {
            // Using delay because DB queries run fast, so loadMoreView flashes in and out quickly.
            LaunchTypes.NORMAL -> launchAfterDelay(1000) {
                getAllLaunches()
            }
            LaunchTypes.LAUNCHPAD -> launchAfterDelay(1000) {
                getLaunchesForLaunchPad(state.siteId)
            }
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initState: LaunchesState): LaunchesViewModel
    }
}
