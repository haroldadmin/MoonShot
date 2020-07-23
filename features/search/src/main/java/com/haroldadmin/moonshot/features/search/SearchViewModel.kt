package com.haroldadmin.moonshot.features.search

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.SearchQuery
import com.haroldadmin.moonshotRepository.search.SearchLaunchesUseCase
import com.haroldadmin.moonshotRepository.search.SearchLaunchpadsUseCase
import com.haroldadmin.moonshotRepository.search.SearchRocketsUseCase
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class SearchViewModel @AssistedInject constructor(
    @Assisted initState: SearchState,
    private val searchLaunchesUseCase: SearchLaunchesUseCase,
    private val searchRocketsUseCase: SearchRocketsUseCase,
    private val searchLaunchpadsUseCase: SearchLaunchpadsUseCase
) : MoonShotViewModel<SearchState>(initState) {

    fun searchFor(searchQuery: String, limit: Int = 10) = viewModelScope.launch {
        searchQuery
            .takeIf { it.isNotBlank() }
            ?.let { query ->
                setState { copy(query = query) }
                searchLaunches(query, limit)
                searchRockets(query, limit)
                searchLaunchpads(query, limit)
            }
            ?: setState {
                copy(
                    query = searchQuery,
                    launches = Resource.Uninitialized,
                    launchPads = Resource.Uninitialized,
                    rockets = Resource.Uninitialized
                )
            }
    }

    suspend fun searchLaunches(query: String, limit: Int) = viewModelScope.launch {
        searchLaunchesUseCase
            .searchFor(SearchQuery(query), limit)
            .collect { launchesRes ->
                setState { copy(launches = launchesRes) }
            }
    }

    suspend fun searchRockets(query: String, limit: Int) = viewModelScope.launch {
        searchRocketsUseCase
            .searchFor(SearchQuery(query), limit)
            .collect { rocketsRes ->
                setState { copy(rockets = rocketsRes) }
            }
    }

    suspend fun searchLaunchpads(query: String, limit: Int) = viewModelScope.launch {
        searchLaunchpadsUseCase
            .searchFor(SearchQuery(query), limit)
            .collect { launchPadsRes ->
                setState { copy(launchPads = launchPadsRes) }
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initState: SearchState): SearchViewModel
    }
}