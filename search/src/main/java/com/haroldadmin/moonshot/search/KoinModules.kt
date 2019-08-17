package com.haroldadmin.moonshot.search

import com.haroldadmin.moonshotRepository.search.SearchLaunchesUseCase
import com.haroldadmin.moonshotRepository.search.SearchLaunchpadsUseCase
import com.haroldadmin.moonshotRepository.search.SearchRocketsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val searchModule = module {
    viewModel { (initState: SearchState) -> SearchViewModel(initState, get<SearchLaunchesUseCase>(), get<SearchRocketsUseCase>(), get<SearchLaunchpadsUseCase>()) }
}