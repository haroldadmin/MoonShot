package com.haroldadmin.moonshot.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.base.koin
import com.haroldadmin.moonshotRepository.search.SearchLaunchesUseCase
import com.haroldadmin.moonshotRepository.search.SearchLaunchpadsUseCase
import com.haroldadmin.moonshotRepository.search.SearchRocketsUseCase
import com.haroldadmin.vector.VectorViewModelFactory
import com.haroldadmin.vector.ViewModelOwner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class SearchViewModel(
    initState: SearchState,
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
    }

    suspend fun searchLaunches(query: String, limit: Int) = viewModelScope.launch {
        searchLaunchesUseCase
            .searchFor(query, limit)
            .collect { launchesRes ->
                setState { copy(launches = launchesRes) }
            }
    }

    suspend fun searchRockets(query: String, limit: Int) = viewModelScope.launch {
        searchRocketsUseCase
            .searchFor(query, limit)
            .collect { rocketsRes ->
                setState { copy(rockets = rocketsRes) }
            }
    }

    suspend fun searchLaunchpads(query: String, limit: Int) = viewModelScope.launch {
        searchLaunchpadsUseCase
            .searchFor(query, limit)
            .collect { launchPadsRes ->
                setState { copy(launchPads = launchPadsRes) }
            }
    }

    companion object : VectorViewModelFactory<SearchViewModel, SearchState> {
        override fun create(
            initialState: SearchState,
            owner: ViewModelOwner,
            handle: SavedStateHandle
        ): SearchViewModel? = with(owner.koin()) {
            SearchViewModel(initialState, get(), get(), get())
        }
    }
}