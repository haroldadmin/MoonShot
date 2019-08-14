package com.haroldadmin.moonshot.search

import com.haroldadmin.moonshotRepository.search.SearchLaunchesUseCase
import com.haroldadmin.moonshotRepository.search.SearchLaunchpadsUseCase
import com.haroldadmin.moonshotRepository.search.SearchRocketsUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    viewModel { (initState: SearchState) -> SearchViewModel(initState, get<SearchLaunchesUseCase>(), get<SearchRocketsUseCase>(), get<SearchLaunchpadsUseCase>()) }
}